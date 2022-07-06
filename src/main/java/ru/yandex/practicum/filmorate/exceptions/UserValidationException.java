package ru.yandex.practicum.filmorate.exceptions;

public class UserValidationException extends ValidationException {
    public UserValidationException(final String message) {
        super(message);
    }

    public UserValidationException(final String message, Throwable cause) {
        super(message, cause);
    }
}
