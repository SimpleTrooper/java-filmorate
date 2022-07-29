package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.links.Like;
import ru.yandex.practicum.filmorate.storage.links.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация хранилища лайков в БД
 */
@Component("likesDbStorage")
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Like> getAll() {
        String sql = "SELECT film_id, user_id FROM likes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs));
    }

    @Override
    public List<Long> getListOfSecondIdByFirstId(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }

    @Override
    public Like getByKey(Long filmId, Long userId) {
        String sql = "SELECT film_id, user_id FROM likes WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeLike(rs), filmId, userId);
    }

    @Override
    public Like add(Like like) {
        if (like == null) {
            return null;
        }
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, like.getFilmId(), like.getUserId());
        return like;
    }

    @Override
    public boolean remove(Like like) {
        if (like == null) {
            return false;
        }
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, like.getFilmId(), like.getUserId()) > 0;
    }

    @Override
    public boolean removeLikesByFilmId(Long filmId) {
        String sql = "DELETE FROM likes WHERE film_id = ? ";
        return jdbcTemplate.update(sql, filmId) > 0;
    }

    private Like makeLike(ResultSet resultSet) throws SQLException {
        return new Like(resultSet.getLong("film_id"), resultSet.getLong("user_id"));
    }
}
