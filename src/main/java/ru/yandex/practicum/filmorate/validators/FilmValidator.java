package ru.yandex.practicum.filmorate.validators;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

/*
Ручная валидация данных о фильме
 */
public class FilmValidator {
    public static void validate(Film film) {
        validateReleaseDate(film);
    }

    public static void validateReleaseDate(Film film) {
        if (film.getReleaseDate() != null &&
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("Ошибка валидации даты релиза фильма." +
                    " Дата релиза не может быть раньше 28.12.1895.");
        }
    }
}
