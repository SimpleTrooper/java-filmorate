package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

/**
 * Логика работы с жанрами фильмов
 */
@Service
public class GenreService extends DataService<GenreStorage, Genre> {
    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        super(genreStorage);
    }
}
