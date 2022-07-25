package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.links.FilmGenre;
import ru.yandex.practicum.filmorate.model.links.Like;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final LikesStorage likesStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("filmGenreDbStorage") FilmGenreStorage filmGenreStorage,
                         @Qualifier("mpaRatingDbStorage") MpaRatingStorage mpaRatingStorage,
                         @Qualifier("likesDbStorage") LikesStorage likesStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreStorage = filmGenreStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.likesStorage = likesStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        checkMpaRatingExistence(film);
        checkGenresExistence(film);
        checkUsersExistence(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        film.getGenres()
                .forEach(genre -> filmGenreStorage.add(new FilmGenre(filmId, genre.getId())));
        film.getUserLikes()
                .forEach(userId -> likesStorage.add(new Like(filmId, userId)));
        film.setId(filmId);
        return film;
    }

    @Override
    public Film update(Film film) {
        Long filmId = film.getId();
        Film toUpdate = findById(filmId);
        if (toUpdate == null) {
            throw new NotFoundException("Не найден фильм с id = " + filmId);
        }
        checkMpaRatingExistence(film);
        checkGenresExistence(film);
        checkUsersExistence(film);
        filmGenreStorage.removeGenresByFilmId(filmId);
        likesStorage.removeLikesByFilmId(filmId);

        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpaRating().getId());
        return film;
    }

    private void checkMpaRatingExistence(Film film) {
        if (film.getMpaRating() != null) {
            if (mpaRatingStorage.findById(film.getMpaRating().getId()) == null) {
                throw new NotFoundException("Не найден рейтинг MPA с id = " + film.getMpaRating().getId());
            }
        }
    }

    private void checkUsersExistence(Film film) {
        film.getUserLikes()
                .forEach(userId -> {
                    try {
                        userStorage.findById(userId);
                    } catch (DataAccessException ex) {
                        throw new NotFoundException("Не найден пользователь с id = " + userId);
                    }
                });
    }

    private void checkGenresExistence(Film film) {
        film.getGenres()
                .forEach(genre -> {
                    if (genreStorage.findById(genre.getId()) == null) {
                        throw new NotFoundException("Не найден жанр с id = " + genre.getId());
                    }
                });
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT film_id, name, description, release_date, duration, mpa_rating_id FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findById(Long filmId) {
        String sql = "SELECT film_id, name, description, release_date, duration, mpa_rating_id " +
                "FROM films  WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), filmId);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        Long filmId = resultSet.getLong("film_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Date releaseDate = resultSet.getDate("release_date");
        Integer duration = resultSet.getInt("duration");
        List<Long> userLikes = likesStorage.getUsersByFilmId(filmId);
        Long mpaRatingId = resultSet.getLong("mpa_rating_id");
        MpaRating mpaRating = mpaRatingStorage.findById(mpaRatingId);
        List<Genre> genres = filmGenreStorage.getGenresByFilmId(filmId);
        Film film = Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .duration(duration)
                .mpaRating(mpaRating)
                .releaseDate(releaseDate.toLocalDate())
                .build();
        film.addAllLikes(userLikes);
        film.addAllGenres(genres);
        return film;
    }
}
