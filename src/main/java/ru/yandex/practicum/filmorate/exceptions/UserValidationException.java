package ru.yandex.practicum.filmorate.exceptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException(final String message) {
        super(message);
    }

    public UserValidationException(final String message, Throwable cause) {
        super(message, cause);
    }
}
