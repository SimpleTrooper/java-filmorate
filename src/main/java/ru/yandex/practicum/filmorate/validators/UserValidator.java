package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.User;

/**
 * Ручная валидация данных о пользователе
 */
public class UserValidator {
    public static void validate(User user) {
        validateAndSetName(user);
    }

    //Валидация имени пользователя. Если имя отсутствует - используется логин.
    public static void validateAndSetName(User user) {
        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }
    }
}
