package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;

@SpringBootTest
@Sql({"/schema.sql", "/data-test-genres.sql", "/data-test-mpa.sql"})
public class FilmDbStorageTest extends StorageTest<Film> {
    @Autowired
    public FilmDbStorageTest(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        super(filmStorage);
    }

    @BeforeAll
    static void init() {
    }

    @BeforeEach
    @Override
    void initData() {
        data1 = Film.builder()
                .name("Test film")
                .description("Test description")
                .duration(120)
                .releaseDate(LocalDate.of(1990, 10, 10))
                .mpa(new MpaRating((long) 1, "G"))
                .build();
        data1.addGenre(new Genre(1L,"Комедия"));

        data2 = Film.builder()
                .name("Terminator")
                .description("Science fiction with some action")
                .duration(107)
                .releaseDate(LocalDate.of(1984, 10, 24))
                .mpa(new MpaRating((long) 4, "R"))
                .build();
        data2.addGenre(new Genre(6L,"Боевик"));

        data3 = Film.builder()
                .name("The Green Mile")
                .description("Some description")
                .duration(189)
                .releaseDate(LocalDate.of(1999, 12, 6))
                .mpa(new MpaRating((long) 4, "R"))
                .build();
        data3.addGenre(new Genre(2L,"Драма"));
    }
}
