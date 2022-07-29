package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

/**
 * Контроллер жанров
 */
@RestController
@RequestMapping("/genres")
public class GenreController extends DataController<Genre> {

    @Autowired
    public GenreController(GenreService genreService) {
        super(genreService);
    }

    @Override
    protected void validate(Genre genre) {
    }
}
