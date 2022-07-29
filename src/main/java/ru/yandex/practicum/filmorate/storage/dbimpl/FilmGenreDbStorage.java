package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.links.FilmGenre;
import ru.yandex.practicum.filmorate.storage.links.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация хранилища связи жанров и фильмов в БД
 */
@Component("filmGenreDbStorage")
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<FilmGenre> getAll() {
        String sql = "SELECT film_id, genre_id FROM film_genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmGenre(rs));
    }

    @Override
    public List<Long> getListOfSecondIdByFirstId(Long firstId) {
        String sql = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("genre_id"), firstId);
    }

    @Override
    public FilmGenre getByKey(Long filmId, Long genreId) {
        String sql = "SELECT film_id, genre_id FROM film_genre WHERE film_id = ? AND genre_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilmGenre(rs), filmId, genreId);
    }

    @Override
    public FilmGenre add(FilmGenre filmGenre) {
        if (filmGenre == null) {
            return null;
        }
        String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmGenre.getFilmId(), filmGenre.getGenreId());
        return filmGenre;
    }

    @Override
    public boolean remove(FilmGenre filmGenre) {
        if (filmGenre == null) {
            return false;
        }
        String sql = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
        return jdbcTemplate.update(sql, filmGenre.getFilmId(), filmGenre.getGenreId()) > 0;
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }

    @Override
    public boolean removeGenresByFilmId(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.update(sql, filmId) > 0;
    }

    private FilmGenre makeFilmGenre(ResultSet resultSet) throws SQLException {
        return new FilmGenre(resultSet.getLong("film_id"), resultSet.getLong("genre_id"));
    }
}
