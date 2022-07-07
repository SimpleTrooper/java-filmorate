package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.List;

/**
 * Контроллер пользователей
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends DataController<User> {
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @Override
    protected void validate(User user) {
        UserValidator.validate(user);
    }

    /**
     * Обработчик эндпоинта добавления пользователей в друзья
     *
     * @param userId   - ID пользователя
     * @param friendId - ID друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        log.info("Попытка добавить в друзья пользователей с id = {}, {}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Добавлены в друзья пользователи с id = {}, {}", userId, friendId);
    }

    /**
     * Обработчик эндпоинта удаления пользователей из друзей
     *
     * @param userId   - ID пользователя
     * @param friendId - ID друга
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        log.info("Попытка удалить из друзей пользователей с id = {}, {}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("Удалены из друзей пользователи с id = {}, {}", userId, friendId);
    }

    /**
     * Обработчик эндпоинта получения всех друзей пользователя
     *
     * @param userId - ID пользователя
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable(name = "id") Long userId) {
        log.info("Попытка вернуть список друзей пользователя с id = {}", userId);
        List<User> friends = userService.findFriends(userId);
        log.info("Получен список друзей пользователя с id = {}", userId);
        return friends;
    }

    /**
     * Обработчик эндпоинта получения общих друзей пользователей
     *
     * @param userId  - ID пользователя
     * @param otherId - ID второго пользователя
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable(name = "id") Long userId, @PathVariable Long otherId) {
        log.info("Попытка вернуть список общих друзей пользователей с id = {}, {}", userId, otherId);
        List<User> mutualFriends = userService.findMutualFriends(userId, otherId);
        log.info("Получен список общих друзей пользователей с id = {}, {}", userId, otherId);
        return mutualFriends;
    }
}
