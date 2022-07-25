package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.links.DataLink;

import java.util.List;

public interface DataLinkStorage<T extends DataLink> {
    List<T> getAll();

    List<Long> getListOfSecondIdByFirstId(Long firstId);

    T getByKey(Long firstId, Long secondId);

    T add(T dataLink);

    boolean remove(T dataLink);
}
