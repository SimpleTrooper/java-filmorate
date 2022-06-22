package ru.yandex.practicum.filmorate.validators;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmValidator {
    private static final int MAX_FILM_DESCRIPTION_LENGTH = 200;

    public static void validate(Film film) {
        validateName(film);
        validateDescription(film);
        validateReleaseDate(film);
        validateDuration(film);
    }

    public static void validateName(Film film) {
        if (film.getName() == null || film.getName().length() == 0) {
            throw new FilmValidationException("Ошибка валидации названия фильма. Название не может быть пустым.");
        }
    }

    public static void validateDescription(Film film) {
        if (film.getDescription() != null && film.getDescription().length() > MAX_FILM_DESCRIPTION_LENGTH) {
            throw new FilmValidationException("Ошибка валидации описания фильма." +
                    " Максимальная длина описания - 200 символов.");
        }
    }

    public static void validateReleaseDate(Film film) {
        if (film.getReleaseDate() != null &&
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("Ошибка валидации даты релиза фильма." +
                    " Дата релиза не может быть раньше 28.12.1895.");
        }
    }

    public static void validateDuration(Film film) {
        if (film.getDuration() <= 0) {
            throw new FilmValidationException("Ошибка валидации продолжительности фильма." +
                    " Продолжительность должна быть положительной.");
        }
    }
}
