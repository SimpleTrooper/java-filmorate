package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.DataEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Абстрактный класс для тестирования хранилища данных
 * @param <T> класс данных
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class StorageTest<T extends DataEntity> {
    protected final Storage<T> storage;
    protected T data1, data2, data3;

    abstract void initData();

    /**
     * Стандартное поведение add(DataEntity)
     */
    @Test
    void shouldAddData() {
        T newData = storage.add(data1);
        data1.setId(newData.getId());

        T actual = storage.findById(newData.getId());
        assertThat(actual).isNotNull()
                .isEqualTo(data1);
    }

    /**
     * Поведение add(DataEntity) при добавлении null в хранилище
     */
    @Test
    void shouldNotAddNull() {
        T actual = storage.add(null);
        assertThat(actual).isNull();
    }

    /**
     * Стандартное поведение update(DataEntity) при обновлении записи
     */
    @Test
    void shouldUpdateData() {
        T newData = storage.add(data1);
        data2.setId(newData.getId());
        storage.update(data2);

        T actual = storage.findById(data2.getId());
        assertThat(actual).isNotNull()
                .isEqualTo(data2);
    }

    /**
     * Поведение update(DataEntity) при некорректном ID
     */
    @Test
    void shouldNotUpdateWithWrongId() {
        T newData = storage.add(data1);
        data1.setId(newData.getId());
        List<T> dataList = new ArrayList<>();
        dataList.add(data1);

        data2.setId((long) -1);
        assertThatThrownBy(() -> storage.update(data2))
                .isInstanceOf(NotFoundException.class);

        List<T> actual = storage.findAll();
        assertThat(actual).hasSize(1)
                .isEqualTo(dataList);
    }

    /**
     * Стандартное поведение findAll() при выводе всех записей
     */
    @Test
    void shouldFindAll() {
        data1.setId(storage.add(data1).getId());
        data2.setId(storage.add(data2).getId());
        data3.setId(storage.add(data3).getId());

        List<T> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        List<T> actual = storage.findAll();
        assertThat(actual).isNotNull()
                .hasSize(3)
                .isEqualTo(dataList);
    }

    /**
     * Стандартное поведение findById(Long) при выводе записи по ID
     */
    @Test
    void shouldFindById() {
        T newData = storage.add(data1);
        data1.setId(newData.getId());

        T actual = storage.findById(newData.getId());
        assertThat(actual).isNotNull()
                .isEqualTo(data1);
    }

    /**
     * Поведение findById(Long) при некорректном ID
     */
    @Test
    void shouldNotFindWithWrongId() {
        assertThatThrownBy(() -> storage.findById((long) -1))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    /**
     * Стандартное поведение contains(Long) при проверке записи на существование в хранилище по ID - true
     */
    @Test
    void shouldReturnTrueIfContains() {
        T newData = storage.add(data1);
        data1.setId(newData.getId());

        boolean actual = storage.contains(newData.getId());
        assertThat(actual).isTrue();
    }

    /**
     * Поведение contains(Long) при отсутствии записи - возвращаем false
     */
    @Test
    void shouldReturnFalseIfNotContains() {
        boolean actual = storage.contains(-1L);
        assertThat(actual).isFalse();
    }
}
