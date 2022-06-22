package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdGen;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.validators.UserValidator;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final IdGen idGen = new IdGen();
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            UserValidator.validate(user);
        } catch (UserValidationException ex) {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int nextFilmId = idGen.nextId();
        user.setId(nextFilmId);
        users.put(nextFilmId, user);
        log.info("Добавлен новый пользователь: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        int userId = user.getId();
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка обновить пользователя с несуществующим id={}", userId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            UserValidator.validate(user);
        } catch (UserValidationException ex) {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setId(userId);
        users.put(userId, user);
        log.info("Обновлен пользователь c id={}: {}", userId, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
