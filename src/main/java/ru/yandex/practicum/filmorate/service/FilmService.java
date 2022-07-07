package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Логика работы с фильмами
 */
@Service
public class FilmService extends DataService<FilmStorage, Film> {
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
    }

    /**
     * Добавление лайка фильму
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    public void addLike(Long filmId, Long userId) {
        Film film = storage.findById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Попытка поставить лайк фильму с id=%d пользователем с id=%d. " +
                    "Фильм с id=%d не найден", filmId, userId, filmId));
        }
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Попытка поставить лайк фильму с id=%d пользователем с id=%d. " +
                    "Пользователь с id=%d не найден", filmId, userId, userId));
        }
        film.addLike(userId);
    }

    /**
     * Удаление лайка фильму
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    public void removeLike(Long filmId, Long userId) {
        Film film = storage.findById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Попытка удалить лайк фильму с id=%d пользователя с id=%d. " +
                    "Фильм с id=%d не найден", filmId, userId, filmId));
        }
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Попытка удалить лайк фильму с id=%d пользователя с id=%d. " +
                    "Пользователь с id=%d не найден", filmId, userId, userId));
        }
        film.removeLike(userId);
    }

    /**
     * Возвращение n фильмов с наибольшим числом лайков
     * @param n - количество фильмов с наибольшим числом лайков
     */
    public List<Film> getTopN(int n) {
        return storage.findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(n).collect(Collectors.toList());
    }
}
