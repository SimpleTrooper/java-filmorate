package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.links.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.links.LikesStorage;
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
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage")  UserStorage userStorage,
                       @Qualifier("likesDbStorage") LikesStorage likesStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    /**
     * Добавление лайка фильму
     *
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    public void addLike(Long filmId, Long userId) {
        if (!storage.contains(filmId)) {
            throw new NotFoundException(String.format("Попытка поставить лайк фильму с id=%d пользователем с id=%d. " +
                    "Фильм с id=%d не найден", filmId, userId, filmId));
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException(String.format("Попытка поставить лайк фильму с id=%d пользователем с id=%d. " +
                    "Пользователь с id=%d не найден", filmId, userId, userId));
        }
        likesStorage.add(new Like(filmId, userId));
    }

    /**
     * Удаление лайка фильму
     *
     * @param filmId - ID фильма
     * @param userId - ID пользователя
     */
    public void removeLike(Long filmId, Long userId) {
        if (!storage.contains(filmId)) {
            throw new NotFoundException(String.format("Попытка удалить лайк фильму с id=%d пользователя с id=%d. " +
                    "Фильм с id=%d не найден", filmId, userId, filmId));
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException(String.format("Попытка удалить лайк фильму с id=%d пользователя с id=%d. " +
                    "Пользователь с id=%d не найден", filmId, userId, userId));
        }
        likesStorage.remove(new Like(filmId, userId));
    }

    /**
     * Возвращение n фильмов с наибольшим числом лайков
     *
     * @param n - количество фильмов с наибольшим числом лайков
     */
    public List<Film> getTopN(int n) {
        return storage.findAll().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(n).collect(Collectors.toList());
    }
}
