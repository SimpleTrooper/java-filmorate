package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.links.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        String sql = "SELECT friend_id FROM friendship WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFriendship(rs), userId, friendId);
    }

    @Override
    public Friendship add(Friendship friendship) {
        String sql = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId(), friendship.isStatus());
        return friendship;
    }

    @Override
    public boolean remove(Friendship friendship) {
        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId()) > 0;
    }

    @Override
    public void setFriendshipStatusConfirmed(Friendship friendship) {
        setFriendshipStatus(friendship, true);
    }

    @Override
    public void setFriendshipStatusNotConfirmed(Friendship friendship) {
        setFriendshipStatus(friendship, false);
    }

    private void setFriendshipStatus(Friendship friendship, boolean status) {
        String sql = "UPDATE friendship SET status = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, status, friendship.getUserId(), friendship.getFriendId());
    }

    private Friendship makeFriendship(ResultSet resultSet) throws SQLException {
        return new Friendship(resultSet.getLong("user_id"), resultSet.getLong("friend_id"),
                resultSet.getBoolean("status"));
    }
}
