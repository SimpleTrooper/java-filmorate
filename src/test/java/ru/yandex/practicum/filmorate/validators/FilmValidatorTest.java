package ru.yandex.practicum.filmorate.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmValidatorTest {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static Film film;

    @BeforeEach
    public void init() {
        film = new Film();
        film.setName("Name");
        String description = "d".repeat(MAX_DESCRIPTION_LENGTH);
        film.setDescription(description);
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
    }

    /*
    Стандартное поведение валидации названия
     */
    @Test
    public void shouldValidateNonEmptyName() {
        assertDoesNotThrow(() -> FilmValidator.validateName(film),
                "Выбрасывается исключение при правильном названии фильма");
    }

    /*
    Поведение при пустой строке названия фильма
     */
    @Test
    public void shouldNotValidateEmptyName() {
        film.setName("");
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateName(film),
                "Не выбрасывается исключение при пустом названии фильма");
    }

    /*
    Поведение при названии = null
     */
    @Test
    public void shouldNotValidateNullName() {
        film.setName(null);
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateName(film),
                "Не выбрасывается исключение при названии фильма = null");
    }

    /*
    Стандартное поведение валидации описания
     */
    @Test
    public void shouldValidateDescriptionEquals200() {
        assertDoesNotThrow(() -> FilmValidator.validateDescription(film),
                "Выбрасывается исключение при длине описания фильма равной 200 символов");
    }

    /*
    Поведение при длине описания фильма = макс. длина + 1
     */
    @Test
    public void shouldNotValidateDescriptionMoreThan200() {
        film.setDescription(film.getDescription() + "d");
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateDescription(film),
                "Не выбрасывается исключение при слишком длинном описании фильма");
    }

    /*
    Стандартное поведение валидации даты релиза
     */
    @Test
    public void shouldValidateReleaseDateEqualsCinemaBirthday() {
        assertDoesNotThrow(() -> FilmValidator.validateReleaseDate(film),
                "Выбрасывается исключение при дате релиза равном дню рождения кино");
    }

    /*
    Поведение при дате релиза раньше, чем день рождения кино
     */
    @Test
    public void shouldNotValidateReleaseDateEarlyThanCinemaBirthday() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateReleaseDate(film),
                "Не выбрасывается исключение при дате релиза раньше, чем день рождения кино");
    }

    /*
    Стандартное поведение валидации продолжительности
     */
    @Test
    public void shouldValidatePositiveDuration() {
        assertDoesNotThrow(() -> FilmValidator.validateDuration(film),
                "Выбрасывается исключение при положительной продолжительности");
    }

    /*
   Поведение при нулевой продолжительности
    */
    @Test
    public void shouldNotValidateZeroDuration() {
        film.setDuration(0);
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateDuration(film),
                "Не выбрасывается исключение при нулевой продолжительности");
    }

    /*
   Поведение при отрицательной продолжительности
    */
    @Test
    public void shouldNotValidateNegativeDuration() {
        film.setDuration(-1);
        assertThrows(FilmValidationException.class, () -> FilmValidator.validateDuration(film),
                "Не выбрасывается исключение при отрицательной продолжительности");
    }
}
