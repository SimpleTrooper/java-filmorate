package ru.yandex.practicum.filmorate.storage.links;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.links.FilmGenre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования связи фильм-жанр
 */
@Sql({"/schema.sql", "/data-test-mpa.sql", "/data-test-genres.sql", "/data-test-films.sql"})
public class FilmGenreDbStorageTest extends DataLinkStorageTest<FilmGenre> {
    private final GenreStorage genreStorage;

    public FilmGenreDbStorageTest(@Qualifier("filmGenreDbStorage") FilmGenreStorage filmGenreStorage,
                                  @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        super(filmGenreStorage);
        this.genreStorage = genreStorage;
    }

    @BeforeEach
    @Override
    void initData() {
        data1 = new FilmGenre(1L, 1L);
        data2 = new FilmGenre(1L, 2L);
        data3 = new FilmGenre(2L, 1L);
    }

    /**
     * Стандартное поведение получения списка жанров по ID фильма
     */
    @Test
    void shouldGetGenresByFilmId() {
        FilmGenreStorage filmGenreStorage = (FilmGenreStorage) storage;
        storage.add(data1);
        storage.add(data2);
        List<Genre> genres = List.of(genreStorage.findById(data1.getGenreId()),
                genreStorage.findById(data2.getGenreId()));
        List<Genre> actual = filmGenreStorage.getGenresByFilmId(data1.getFilmId());
        assertThat(actual).isNotNull()
                .hasSize(2)
                .isSubsetOf(genres);
    }

    /**
     * Поведение получения списка жанров по ID фильма при некорректном ID
     */
    @Test
    void shouldGetEmptyListOfGenresWhenIncorrectId() {
        FilmGenreStorage filmGenreStorage = (FilmGenreStorage) storage;
        storage.add(data1);
        storage.add(data2);
        List<Genre> actual = filmGenreStorage.getGenresByFilmId(-1L);
        assertThat(actual).isNotNull()
                .hasSize(0);
    }

    /**
     * Стандартное поведение при удалении списка жанров по ID фильма
     */
    @Test
    void shouldRemoveGenresByFilmId() {
        FilmGenreStorage filmGenreStorage = (FilmGenreStorage) storage;
        storage.add(data1);
        storage.add(data2);
        boolean actual = filmGenreStorage.removeGenresByFilmId(data1.getFilmId());
        assertThat(actual).isTrue();
        List<Genre> actualGenres = filmGenreStorage.getGenresByFilmId(data1.getFilmId());
        assertThat(actualGenres).isNotNull()
                .hasSize(0);
    }

    /**
     * Поведение при удалении списка жанров по ID фильма с некорректным ID
     */
    @Test
    void shouldNotRemoveGenresByFilmIdWhenIncorrectId() {
        FilmGenreStorage filmGenreStorage = (FilmGenreStorage) storage;
        storage.add(data1);
        storage.add(data2);
        List<Genre> genres = List.of(genreStorage.findById(data1.getGenreId()),
                genreStorage.findById(data2.getGenreId()));
        boolean actual = filmGenreStorage.removeGenresByFilmId(-1L);
        assertThat(actual).isFalse();
        List<Genre> actualGenres = filmGenreStorage.getGenresByFilmId(data1.getFilmId());
        assertThat(actualGenres).isNotNull()
                .hasSize(2)
                .isSubsetOf(genres);
    }
}
