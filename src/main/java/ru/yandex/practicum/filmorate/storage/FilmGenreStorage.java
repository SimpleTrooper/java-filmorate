package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.links.FilmGenre;

import java.util.List;

public interface FilmGenreStorage extends DataLinkStorage<FilmGenre> {
    default List<Long> getGenresIdByFilmId(Long filmId) {
        return getListOfSecondIdByFirstId(filmId);
    }

    List<Genre> getGenresByFilmId(Long filmId);

    boolean removeGenresByFilmId(Long filmId);
}
