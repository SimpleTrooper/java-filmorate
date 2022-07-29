package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import ru.yandex.practicum.filmorate.storage.links.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.links.LikesStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация хранилища фильмов в БД
 */
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
        if (film == null) {
            return null;
        }
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
        if (film == null) {
            return null;
        }
        Long filmId = film.getId();
        checkFilm(film);
        filmGenreStorage.removeGenresByFilmId(filmId);
        likesStorage.removeLikesByFilmId(filmId);

        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?";
        Long mpaRatingId = film.getMpa().getId();
        film.getGenres()
                .forEach(genre -> filmGenreStorage.add(new FilmGenre(filmId, genre.getId())));
        film.getUserLikes()
                .forEach(userId -> likesStorage.add(new Like(filmId, userId)));
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), mpaRatingId);
        return film;
    }

    private void checkFilm(Film film) {
        checkFilmExistence(film.getId());
        checkMpaRatingExistence(film);
        checkGenresExistence(film);
        checkUsersExistence(film);
    }

    private void checkFilmExistence(Long filmId) {
        if (!contains(filmId)) {
            throw new NotFoundException("Не найден фильм с id = " + filmId);
        }
    }

    private void checkMpaRatingExistence(Film film) {
        Long mpaId = null;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        if (!mpaRatingStorage.contains(mpaId)) {
            throw new NotFoundException("Не найден рейтинг MPA с id = " + mpaId);
        }
    }

    private void checkUsersExistence(Film film) {
        for (Long userId : film.getUserLikes()) {
            if (!userStorage.contains(userId)) {
                throw new NotFoundException("Не найден пользователь с id = " + userId);
            }
        }
    }

    private void checkGenresExistence(Film film) {
        film.getGenres()
                .forEach(genre -> {
                    if (!genreStorage.contains(genre.getId())) {
                        throw new NotFoundException("Не найден жанр с id = " + genre.getId());
                    }
                });
    }

    @Override
    public boolean contains(Long filmId) {
        try {
            findById(filmId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return false;
        }
        return true;
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
        int duration = resultSet.getInt("duration");
        List<Long> userLikes = likesStorage.getUsersByFilmId(filmId);
        Long mpaRatingId = resultSet.getLong("mpa_rating_id");
        MpaRating mpaRating = null;
        if (mpaRatingStorage.contains(mpaRatingId)) {
            mpaRating = mpaRatingStorage.findById(mpaRatingId);
        }
        List<Genre> genres = filmGenreStorage.getGenresByFilmId(filmId);
        Film film = Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .duration(duration)
                .mpa(mpaRating)
                .releaseDate(releaseDate.toLocalDate())
                .build();
        film.addAllLikes(userLikes);
        film.addAllGenres(genres);
        return film;
    }
}
