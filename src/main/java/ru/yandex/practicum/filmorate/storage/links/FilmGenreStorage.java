package ru.yandex.practicum.filmorate.storage.links;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.links.FilmGenre;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем связи жанров и фильмов
 */
public interface FilmGenreStorage extends DataLinkStorage<FilmGenre> {
    default List<Long> getGenresIdByFilmId(Long filmId) {
        return getListOfSecondIdByFirstId(filmId);
    }

    List<Genre> getGenresByFilmId(Long filmId);

    boolean removeGenresByFilmId(Long filmId);
}
