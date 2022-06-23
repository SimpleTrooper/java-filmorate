package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGen;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final IdGen idGen = new IdGen();
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
        } catch (FilmValidationException ex) {
            log.warn(ex.getMessage());
            throw new FilmValidationException(ex.getMessage(), ex);
        }
        int nextFilmId = idGen.nextId();
        film.setId(nextFilmId);
        films.put(nextFilmId, film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            log.warn("Попытка обновить фильм с несуществующим id={}", filmId);
            throw new NotFoundException("Запись с id = " + filmId + " не найдена");
        }
        try {
            FilmValidator.validate(film);
        } catch (FilmValidationException ex) {
            log.warn(ex.getMessage());
            throw new FilmValidationException(ex.getMessage(), ex);
        }
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Обновлен фильм c id={}: {}",filmId, film);
        return film;
    }
}
