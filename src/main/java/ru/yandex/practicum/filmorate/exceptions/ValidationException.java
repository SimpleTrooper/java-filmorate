package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message, Throwable cause) {
        super(message, cause);
    }
}
