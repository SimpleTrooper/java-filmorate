package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Реализация интерфейса для работы с хранилищем фильмов. Хранение в памяти
 */
@Component
public class InMemoryFilmStorage extends InMemoryDataStorage<Film> implements FilmStorage {
}
