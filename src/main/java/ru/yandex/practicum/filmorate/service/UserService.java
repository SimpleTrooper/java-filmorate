package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Логика работы с пользователями
 */
@Service
public class UserService extends DataService<UserStorage, User> {

    @Autowired
    public UserService(UserStorage userStorage) {
        super(userStorage);
    }

    /**
     * Добавление пользователей в друзья
     *
     * @param userId   - ID пользователя
     * @param friendId - ID друга
     */
    public void addFriend(Long userId, Long friendId) {
        User user = storage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Попытка добавить пользователю с id=%d, друга с id=%d. " +
                    "Пользователь с id=%d не найден", userId, friendId, userId));
        }
        User friend = storage.findById(friendId);
        if (friend == null) {
            throw new NotFoundException(String.format("Попытка добавить пользователю с id=%d, друга с id=%d. " +
                    "Пользователь с id=%d не найден", userId, friendId, friendId));
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    /**
     * Удаление пользователей из друзей
     *
     * @param userId   - ID пользователя
     * @param friendId - ID друга
     */
    public void removeFriend(Long userId, Long friendId) {
        User user = storage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Попытка удалить у пользователя с id=%d, друга с id=%d. " +
                    "Пользователь с id=%d не найден", userId, friendId, userId));
        }
        User friend = storage.findById(friendId);
        if (friend == null) {
            throw new NotFoundException(String.format("Попытка удалить у пользователя с id=%d, друга с id=%d. " +
                    "Пользователь с id=%d не найден", userId, friendId, friendId));
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    /**
     * Получение друзей пользователя
     *
     * @param userId - ID пользователя
     * @return список друзей пользователя
     */
    public List<User> findFriends(Long userId) {
        User user = storage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Попытка получить список друзей пользователя с " +
                    "id=%d. Пользователь с id=%d не найден", userId, userId));
        }
        return storage.findAll().stream()
                .filter(x -> user.getFriends().contains(x.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Получение общих друзей пользователей
     *
     * @param firstUserId  - ID первого пользователя
     * @param secondUserId - ID второго пользователя
     * @return список общих друзей пользователей
     */
    public List<User> findMutualFriends(Long firstUserId, Long secondUserId) {
        User firstUser = storage.findById(firstUserId);
        if (firstUser == null) {
            throw new NotFoundException(String.format("Попытка получить список общих друзей пользователей с " +
                    "id=%d, %d. Пользователь с id=%d не найден", firstUserId, secondUserId, firstUserId));
        }
        User secondUser = storage.findById(secondUserId);
        if (secondUser == null) {
            throw new NotFoundException(String.format("Попытка получить список общих друзей пользователей с " +
                    "id=%d, %d. Пользователь с id=%d не найден", firstUserId, secondUserId, secondUserId));
        }
        Set<Long> mutualFriendsId = new HashSet<>(firstUser.getFriends());
        mutualFriendsId.retainAll(secondUser.getFriends());
        return storage.findAll().stream()
                .filter(x -> mutualFriendsId.contains(x.getId()))
                .collect(Collectors.toList());
    }
}
