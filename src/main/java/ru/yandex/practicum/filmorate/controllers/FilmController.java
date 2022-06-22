package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
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
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
        } catch (FilmValidationException ex) {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int nextFilmId = idGen.nextId();
        film.setId(nextFilmId);
        films.put(nextFilmId, film);
        log.info("Добавлен новый фильм: {}", film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            log.warn("Попытка обновить фильм с несуществующим id={}", filmId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            FilmValidator.validate(film);
        } catch (FilmValidationException ex) {
            log.warn(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Обновлен фильм c id={}: {}",filmId, film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}
