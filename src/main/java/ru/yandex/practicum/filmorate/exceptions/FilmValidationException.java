package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends ValidationException {
    public FilmValidationException(final String message) {
        super(message);
    }

    public FilmValidationException(final String message, Throwable cause) {
        super(message, cause);
    }
}
