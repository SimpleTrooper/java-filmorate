package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

@RestController
@RequestMapping("/films")
public class FilmController extends DataController<Film>{
    @Override
    protected void validate(Film film) {
        FilmValidator.validate(film);
    }
}
