package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.List;


/**
 * Контроллер фильмов
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends DataController<Film>{
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService service) {
        super(service);
        this.filmService = service;
    }

    @Override
    protected void validate(Film film) {
        FilmValidator.validate(film);
    }

    /**
     * Обработчик эндпоинта добавления лайка фильму
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        log.info("Попытка поставить лайк фильму с id = {} пользователем c id = {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Добавлен лайк фильму с id = {} пользователем с id = {}", filmId, userId);
    }

    /**
     * Обработчик эндпоинта удаления лайка фильму
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        log.info("Попытка удалить лайк фильму с id = {} пользователем c id = {}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("Удален лайк фильму с id = {} пользователем с id = {}", filmId, userId);
    }

    /**
     * Обработчик эндпоинта вывода фильмов с наибольшим числом лайков
     * @param count - количество фильмов с наибольшим числом лайков
     */
    @GetMapping("/popular")
    public List<Film> getTopN(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Попытка получить {} фильмов с наибольшим числом лайков", count);
        List<Film> topFilms = filmService.getTopN(count);
        log.info("Получены {} фильмов с наибольшим числом лайков", count);
        return topFilms;
    }
}
