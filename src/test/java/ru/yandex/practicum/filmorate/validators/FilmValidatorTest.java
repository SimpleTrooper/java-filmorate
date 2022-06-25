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
}
