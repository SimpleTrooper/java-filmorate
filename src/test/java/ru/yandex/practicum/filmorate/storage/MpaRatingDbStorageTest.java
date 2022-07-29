package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.MpaRating;

/**
 * Класс для тестирования хранилища MPA рейтингов
 */
@SpringBootTest
@Sql("/schema.sql")
public class MpaRatingDbStorageTest extends StorageTest<MpaRating> {
    public MpaRatingDbStorageTest(@Qualifier("mpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        super(mpaRatingStorage);
    }

    @BeforeEach
    public void initData() {
        data1 = new MpaRating(1L, "G");
        data2 = new MpaRating(2L, "PG");
        data3 = new MpaRating(3L, "PG-13");
    }
}
