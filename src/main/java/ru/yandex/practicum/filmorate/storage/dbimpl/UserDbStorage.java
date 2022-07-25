package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.links.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipStorage friendshipStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public User add(User user) {
        checkFriendsExistence(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.getFriends()
                .forEach(friendId -> {
                    friendshipStorage.add(new Friendship(userId, friendId, true));
                });
        user.setId(userId);
        return user;
    }

    private void checkFriendsExistence(User user) {
        user.getFriends()
                .forEach(friendId -> {
                    try {
                        findById(friendId);
                    } catch (DataAccessException ex) {
                        throw new NotFoundException("Не найден пользователь с id = " + friendId);
                    }
                });
    }

    @Override
    public User update(User user) {
        if (findById(user.getId()) == null) {
            throw new NotFoundException("Не найден пользователь с id = " + user.getId());
        }
        checkFriendsExistence(user);
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.getFriends()
                .forEach(friendId -> {
                    friendshipStorage.add(new Friendship(user.getId(), friendId, true));
                });
        return user;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT user_id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findById(Long userId) {
        String sql = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.addFriends(friendshipStorage.getFriendsByUserId(user.getId()));
        return user;
    }
}
