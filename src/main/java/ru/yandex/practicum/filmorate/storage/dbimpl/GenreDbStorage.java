package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre add(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("genre_id");
        Long genreId = simpleJdbcInsert.executeAndReturnKey(genre.toMap()).longValue();
        return findById(genreId);
    }

    @Override
    public Genre update(Genre genre) {
        String sql = "UPDATE genres SET name = ? WHERE genre_id = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return findById(genre.getId());
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM genres AS g ";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre findById(Long id) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM genres AS g " +
                "WHERE g.genre_id = ?";
        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM genres AS g " +
                "INNER JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(ResultSet resultSet) throws SQLException {
        return new Genre(resultSet.getLong("genre_id"), resultSet.getString("name"));
    }
}
