package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем жанров фильмов
 */
public interface GenreStorage extends Storage<Genre> {
    List<Genre> getGenresByFilmId(Long filmId);
}
