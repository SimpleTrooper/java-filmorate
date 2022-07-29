package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql({"/schema.sql", "/data-test-mpa.sql"})
public class GenreDbStorageTest extends StorageTest<Genre> {
    private final FilmStorage filmStorage;

    @Autowired
    public GenreDbStorageTest(@Qualifier("genreDbStorage") GenreStorage genreStorage,
                              @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        super(genreStorage);
        this.filmStorage = filmStorage;
    }

    @BeforeEach
    @Override
    public void initData() {
        data1 = new Genre(10L, "Test genre");
        data2 = new Genre(11L, "Test genre - 2");
        data3 = new Genre(12L, "Test genre - 3");
    }

    /**
     * Стандартное поведение вывода списка жанров по ID фильма
     */
    @Test
    public void shouldGetGenresByFilmId() {
        Film film = Film.builder()
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.of(2000,10,10))
                .duration(120)
                .mpa(new MpaRating(1L, "G"))
                .build();
        data1.setId(storage.add(data1).getId());
        data2.setId(storage.add(data2).getId());
        film.addGenre(data1);
        film.addGenre(data2);

        List<Genre> genres = new ArrayList<>();
        genres.add(data1);
        genres.add(data2);

        Long filmId = filmStorage.add(film).getId();
        GenreStorage genreStorage = (GenreStorage) storage;
        List<Genre> actual = genreStorage.getGenresByFilmId(filmId);
        assertThat(actual).isNotNull()
                .hasSize(2)
                .isEqualTo(genres);
    }

    /**
     * Поведение вывода списка жанров при некорректном ID (ожидаем пустой список)
     */
    @Test
    public void shouldReturnEmptyGenresListIfIncorrectId() {
        Film film = Film.builder()
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.of(2000,10,10))
                .duration(120)
                .mpa(new MpaRating(1L, "G"))
                .build();
        data1.setId(storage.add(data1).getId());
        data2.setId(storage.add(data2).getId());
        film.addGenre(data1);
        film.addGenre(data2);

        filmStorage.add(film);
        GenreStorage genreStorage = (GenreStorage) storage;
        List<Genre> actual = genreStorage.getGenresByFilmId(-1L);
        assertThat(actual).isNotNull()
                .hasSize(0);
    }
}
