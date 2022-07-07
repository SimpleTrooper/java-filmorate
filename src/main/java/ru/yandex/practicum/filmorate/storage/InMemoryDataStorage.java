package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.DataEntity;
import ru.yandex.practicum.filmorate.util.IdGen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса для работы с хранилищем данных. Хранение в памяти
 */
public abstract class InMemoryDataStorage<T extends DataEntity> implements Storage<T> {
    private final IdGen idGen = new IdGen();
    private final Map<Long, T> data = new LinkedHashMap<>();

    @Override
    public T add(T dataEntity) {
        long nextId = idGen.nextId();
        dataEntity.setId(nextId);
        data.put(nextId, dataEntity);
        return dataEntity;
    }

    @Override
    public T update(T dataEntity) {
        long id = dataEntity.getId();
        if (!data.containsKey(id)) {
            throw new NotFoundException("Запись с id = " + id + " не найдена");
        }
        data.put(dataEntity.getId(), dataEntity);
        return dataEntity;
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public T findById(Long dataEntityId) {
        if (!data.containsKey(dataEntityId)) {
            throw new NotFoundException("Запись с id = " + dataEntityId + " не найдена");
        }
        return data.get(dataEntityId);
    }
}
