package ru.yandex.practicum.filmorate.storage.links;

import ru.yandex.practicum.filmorate.model.links.DataLink;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем связи двух таблиц
 */
public interface DataLinkStorage<T extends DataLink> {
    /**
     * Вернуть все записи из хранилища
     * @return List<T extends DataLink> - список записей
     */
    List<T> getAll();

    /**
     * Вернуть вторые id записей, у которых значение первого id = firstId
     * @param firstId
     * @return List<Long> список id
     */
    List<Long> getListOfSecondIdByFirstId(Long firstId);

    /**
     * Вернуть запись с ключом firstId, secondId
     * @param firstId
     * @param secondId
     * @return добавленная запись
     */
    T getByKey(Long firstId, Long secondId);

    /**
     * Добавить запись в хранилище
     * @param dataLink - добавляемая запись
     * @return - добавленная запись
     */
    T add(T dataLink);

    /**
     * Удалить запись из хранилища
     * @param dataLink - удаляемая запись
     * @return true в случае успеха, false - иначе
     */
    boolean remove(T dataLink);
}
