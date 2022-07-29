package ru.yandex.practicum.filmorate.service;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.DataEntity;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

/**
 * Класс логики обработки абстрактных записей
 *
 * @param <T> Интерфейс - абстрактное хранилище записей
 * @param <V> Тип записи
 */
public abstract class DataService<T extends Storage<V>, V extends DataEntity> {
    protected final T storage;

    public DataService(T storage) {
        this.storage = storage;
    }

    /**
     * Создание новой записи в хранилище
     *
     * @param dataEntity - новая запись
     * @return новая запись с обновленным ID
     */
    public V create(V dataEntity) {
        return storage.add(dataEntity);
    }

    /**
     * Обновление новой записи в хранилище
     *
     * @param dataEntity - новая запись с верным ID
     * @return новая запись
     */
    public V update(V dataEntity) {
        if (!storage.contains(dataEntity.getId())) {
            throw new NotFoundException(String.format("Запись с id=%d не найдена", dataEntity.getId()));
        }
        V updated = storage.update(dataEntity);
        return updated;
    }

    /**
     * Получение всех записей из хранилища
     *
     * @return список всех записей
     */
    public List<V> findAll() {
        return storage.findAll();
    }

    /**
     * Получение всех записи из хранилища по ID
     *
     * @return нужная запись из хранилища
     */
    public V getById(Long id) {
        V data;
        try {
            data = storage.findById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(String.format("Запись с id=%d не найдена", id), ex);
        }
        if (data == null) {
            throw new NotFoundException(String.format("Запись с id=%d не найдена", id));
        }
        return data;
    }
}
