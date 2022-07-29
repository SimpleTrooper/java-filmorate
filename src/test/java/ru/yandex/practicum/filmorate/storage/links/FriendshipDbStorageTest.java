package ru.yandex.practicum.filmorate.storage.links;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.links.Friendship;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Класс для тестов таблицы связи дружбы пользователей
 */
@Sql({"/schema.sql", "/data-test-users.sql"})
public class FriendshipDbStorageTest extends DataLinkStorageTest<Friendship> {
    public FriendshipDbStorageTest(@Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage) {
        super(friendshipStorage);
    }

    @BeforeEach
    @Override
    void initData() {
        data1 = new Friendship(1L, 2L);
        data2 = new Friendship(2L, 1L);
        data3 = new Friendship(1L, 3L);
    }

    /**
     * Стандартное поведение setStatusConfirmed
     */
    @Test
    void shouldSetStatusConfirmed() {
        storage.add(data3);
        assertThat(storage.getByKey(data3.getFirstId(), data3.getSecondId()).isStatus())
                .isFalse();
        FriendshipStorage friendshipStorage = (FriendshipStorage) storage;
        friendshipStorage.setFriendshipStatusConfirmed(data3);
        assertThat(storage.getByKey(data3.getFirstId(), data3.getSecondId()).isStatus())
                .isTrue();
    }

    /**
     * Поведение setStatusConfirmed при некорректном id
     */
    @Test
    void shouldNotSetStatusConfirmedWhenIncorrectId() {
        storage.add(data3);
        assertThat(storage.getByKey(data3.getFirstId(), data3.getSecondId()).isStatus())
                .isFalse();
        FriendshipStorage friendshipStorage = (FriendshipStorage) storage;
        boolean actual = friendshipStorage.setFriendshipStatusConfirmed(new Friendship(-1L, 1L));
        assertThat(actual).isFalse();
        assertThat(storage.getByKey(data3.getFirstId(), data3.getSecondId()).isStatus())
                .isFalse();
    }

    /**
     * Стандартное поведение setStatusNotConfirmed
     */
    @Test
    void shouldSetStatusNotConfirmed() {
        storage.add(data1);
        storage.add(data2);
        assertThat(storage.getByKey(data1.getFirstId(), data1.getSecondId()).isStatus())
                .isTrue();
        FriendshipStorage friendshipStorage = (FriendshipStorage) storage;
        friendshipStorage.setFriendshipStatusNotConfirmed(data1);
        assertThat(storage.getByKey(data1.getFirstId(), data1.getSecondId()).isStatus())
                .isFalse();
    }
    /**
     * Поведение setStatusNotConfirmed при некорректном id
     */
    @Test
    void shouldNotSetStatusNotConfirmedWhenIncorrectId() {
        storage.add(data1);
        storage.add(data2);
        assertThat(storage.getByKey(data1.getFirstId(), data1.getSecondId()).isStatus())
                .isTrue();
        FriendshipStorage friendshipStorage = (FriendshipStorage) storage;
        boolean actual = friendshipStorage.setFriendshipStatusNotConfirmed(new Friendship(-1L, 1L));
        assertThat(actual).isFalse();
        assertThat(storage.getByKey(data1.getFirstId(), data1.getSecondId()).isStatus())
                .isTrue();
    }

}
