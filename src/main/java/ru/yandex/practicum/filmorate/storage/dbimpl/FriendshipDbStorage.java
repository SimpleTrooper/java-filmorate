package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.links.Friendship;
import ru.yandex.practicum.filmorate.storage.links.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация хранилища дружбы пользователей
 */
@Component("friendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Friendship> getAll() {
        String sql = "SELECT user_id, friend_id, status FROM friendship";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs));
    }

    @Override
    public List<Long> getListOfSecondIdByFirstId(Long userId) {
        String sql = "SELECT friend_id FROM friendship WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("friend_id"), userId);
    }

    @Override
    public Friendship getByKey(Long userId, Long friendId) {
        String sql = "SELECT user_id, friend_id, status FROM friendship WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFriendship(rs), userId, friendId);
    }

    @Override
    public Friendship add(Friendship friendship) {
        if (friendship == null) {
            return null;
        }
        String sql = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        try {
            getByKey(friendship.getFriendId(), friendship.getUserId());
        } catch (IncorrectResultSizeDataAccessException ex) {
            jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId(), false);
            return friendship;
        }
        jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId(), true);
        setFriendshipStatusConfirmed(new Friendship(friendship.getFriendId(), friendship.getUserId()));
        return friendship;
    }

    @Override
    public boolean remove(Friendship friendship) {
        if (friendship == null) {
            return false;
        }
        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        if (jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId()) > 0) {
            setFriendshipStatusNotConfirmed(new Friendship(friendship.getFriendId(), friendship.getUserId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean setFriendshipStatusConfirmed(Friendship friendship) {
        return setFriendshipStatus(friendship, true);
    }

    @Override
    public boolean setFriendshipStatusNotConfirmed(Friendship friendship) {
        return setFriendshipStatus(friendship, false);
    }

    private boolean setFriendshipStatus(Friendship friendship, boolean status) {
        if (friendship == null) {
            return false;
        }
        String sql = "UPDATE friendship SET status = ? WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sql, status, friendship.getUserId(), friendship.getFriendId()) > 0;
    }

    private Friendship makeFriendship(ResultSet resultSet) throws SQLException {
        return new Friendship(resultSet.getLong("user_id"), resultSet.getLong("friend_id"),
                resultSet.getBoolean("status"));
    }
}
