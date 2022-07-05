package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

@RestController
@RequestMapping("/users")
public class UserController extends DataController<User>{
    @Override
    protected void validate(User user) {
        UserValidator.validate(user);
    }
}
