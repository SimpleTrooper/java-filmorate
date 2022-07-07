package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.DataEntity;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем данных
 */
public interface Storage<T extends DataEntity> {
    /**
     * Добавить в хранилище
     * @param data новая запись
     * @return новая запись с новым ID
     */
     T add(T data);

    /**
     * Обновить запись в хранилище
     * @param data новая запись с верным ID
     * @return обновленная запись
     */
     T update(T data);

    /**
     * Вернуть все записи из хранилища
     * @return список всех записей
     */
     List<T> findAll();

    /**
     * Вернуть запись по ID
     * @return искомая запись
     */
     T findById(Long id);
}
