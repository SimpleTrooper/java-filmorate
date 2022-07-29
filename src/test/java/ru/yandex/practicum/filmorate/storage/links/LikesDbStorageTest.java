package ru.yandex.practicum.filmorate.storage.links;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.links.Like;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестирования таблицы лайков
 */
@Sql({"/schema.sql", "/data-test-mpa.sql", "/data-test-films.sql", "/data-test-users.sql"})
public class LikesDbStorageTest extends DataLinkStorageTest<Like> {
    public LikesDbStorageTest(@Qualifier("likesDbStorage") LikesStorage likesStorage) {
        super(likesStorage);
    }

    @BeforeEach
    @Override
    void initData() {
        data1 = new Like(1L, 1L);
        data2 = new Like(1L, 2L);
        data3 = new Like(2L, 1L);
    }

    /**
     * Стандартное поведение removeLikesByFilmId
     */
    @Test
    void shouldRemoveLikesByFilmId() {
        storage.add(data1);
        storage.add(data2);
        LikesStorage likesStorage = (LikesStorage) storage;
        boolean actual = likesStorage.removeLikesByFilmId(data1.getFilmId());
        assertThat(actual).isTrue();
        List<Like> actualLikes = likesStorage.getAll();
        assertThat(actualLikes).isNotNull()
                .hasSize(0);
    }

    /**
     * Поведение removeLikesByFilmId при некорректном ID
     */
    @Test
    void shouldNotRemoveLikesWhenIncorrectFilmId() {
        storage.add(data1);
        storage.add(data2);
        LikesStorage likesStorage = (LikesStorage) storage;
        boolean actual = likesStorage.removeLikesByFilmId(-1L);
        assertThat(actual).isFalse();
        List<Like> dataLikes = List.of(data1, data2);
        List<Like> actualLikes = likesStorage.getAll();
        assertThat(actualLikes).isNotNull()
                .hasSize(2)
                .isSubsetOf(dataLikes);
    }
}
