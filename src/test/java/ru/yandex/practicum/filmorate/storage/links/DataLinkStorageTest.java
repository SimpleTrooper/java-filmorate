package ru.yandex.practicum.filmorate.storage.links;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.links.DataLink;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Абстрактный класс тестирования таблицы связей
 * @param <T> - класс таблицы связей
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class DataLinkStorageTest<T extends DataLink> {
    protected final DataLinkStorage<T> storage;
    protected T data1, data2, data3;

    abstract void initData();

    /**
     * Стандартное поведение getAll
     */
    @Test
    void shouldGetAll() {
        storage.add(data1);
        storage.add(data2);
        storage.add(data3);

        List<T> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);

        List<T> actual = storage.getAll();
        assertThat(actual).isNotNull()
                .hasSize(3)
                .isSubsetOf(dataList);
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        storage.add(data1);
        T actual = storage.getByKey(data1.getFirstId(), data1.getSecondId());
        assertThat(actual).isNotNull()
                .isEqualTo(data1);
    }

    /**
     * Поведение при попытке добавить null - ожидаем null
     */
    @Test
    void shouldNotAddNull() {
        T actual = storage.add(null);
        assertThat(actual).isNull();
        List<T> actualAll = storage.getAll();
        assertThat(actualAll).isNotNull()
                .hasSize(0);
    }

    /**
     * Стандартное поведение getByKey
     */
    @Test
    void shouldGetByKey() {
        storage.add(data1);
        T actual = storage.getByKey(data1.getFirstId(), data1.getSecondId());
        assertThat(actual).isNotNull()
                .isEqualTo(data1);
    }

    /**
     * Поведение при отсутствующем ключе - ожидаем исключение
     */
    @Test
    void shouldThrowExceptionWhenIncorrectKey() {
        storage.add(data1);
        assertThatThrownBy(() -> storage.getByKey(-1L, 1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    /**
     * Стандартное поведение remove
     */
    @Test
    void shouldRemove() {
        storage.add(data1);
        boolean actual = storage.remove(data1);
        assertThat(actual).isTrue();
        List<T> actualStorageData = storage.getAll();
        assertThat(actualStorageData).isNotNull()
                .hasSize(0);
    }

    /**
     * Поведение remove при отсутствующем ключе - не ожидаем изменения в БД
     */
    @Test
    void shouldNotRemoveWhenIncorrectKey() {
        storage.add(data1);
        boolean actual = storage.remove(data2);
        assertThat(actual).isFalse();
        List<T> actualAll = storage.getAll();
        assertThat(actualAll).isNotNull()
                .hasSize(1)
                .isEqualTo(List.of(data1));
    }

    /**
     * Стандартное поведение getListOfSecondIdByFirstId
     */
    @Test
    void shouldGetListOfSecondIdByFirstId() {
        data1.setFirstId(1L);
        data1.setSecondId(2L);
        data2.setFirstId(1L);
        data2.setSecondId(3L);
        storage.add(data1);
        storage.add(data2);
        List<Long> expectedData = List.of(2L, 3L);
        List<Long> actual = storage.getListOfSecondIdByFirstId(1L);
        assertThat(actual).isNotNull()
                .hasSize(2)
                .isEqualTo(expectedData);
    }

    /**
     * Поведение getListOfSecondIdByFirstId при некорректном firstId - ожидаем пустой список
     */
    @Test
    void shouldNotGetListOfSecondIdWhenIncorrectFirstId() {
        data1.setFirstId(1L);
        data1.setSecondId(2L);
        data2.setFirstId(1L);
        data2.setSecondId(3L);
        storage.add(data1);
        storage.add(data2);
        List<Long> actual = storage.getListOfSecondIdByFirstId(-1L);
        assertThat(actual).isNotNull()
                .hasSize(0);
    }
}
