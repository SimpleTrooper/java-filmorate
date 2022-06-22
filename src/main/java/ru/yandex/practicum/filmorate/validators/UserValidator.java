package ru.yandex.practicum.filmorate.validators;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserValidator {
    public static void validate(User user) {
        validateEmail(user);
        validateLogin(user);
        validateAndSetName(user);
        validateBirthday(user);
    }

    public static void validateEmail(User user) {
        if (user.getEmail() == null || user.getEmail().length() == 0) {
            throw new UserValidationException("Ошибка валидации email пользователя. Email не может быть пустым.");
        } else if (!user.getEmail().contains("@")) {
            throw new UserValidationException("Ошибка валидации email пользователя. Email должен содержать @.");
        }
    }

    public static void validateLogin(User user) {
        if (user.getLogin() == null || user.getLogin().length() == 0) {
            throw new UserValidationException("Ошибка валидации login пользователя. Login не может быть пустым.");
        } else if (user.getLogin().contains(" ")) {
            throw new UserValidationException("Ошибка валидации login пользователя." +
                    " Login не должен содержать пробелы.");
        }
    }

    //Валидация имени пользователя. Если имя отсутствует - используется логин.
    public static void validateAndSetName(User user) {
        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }
    }

    public static void validateBirthday(User user) {
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidationException("Ошибка валидации дня рождения пользователя. " +
                    "День рождения не может быть в будущем.");
        }
    }
}
