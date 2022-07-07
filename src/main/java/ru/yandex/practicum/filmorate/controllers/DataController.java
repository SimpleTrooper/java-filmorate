package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.DataEntity;
import ru.yandex.practicum.filmorate.service.DataService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

/**
 * Абстрактный контроллер с общими методами
 */
@Slf4j
@RestController
public abstract class DataController<T extends DataEntity> {
    private final DataService<? extends Storage<T>, T> dataService;

    public DataController(DataService<? extends Storage<T>, T> dataService) {
        this.dataService = dataService;
    }

    protected abstract void validate(T dataEntity);

    /**
     * Обработчик эндпоинта, возвращающий все записи
     *
     * @return List<T extends DataEntity>.
     */
    @GetMapping
    public List<T> findAll() {
        return dataService.findAll();
    }

    /**
     * Обработчик эндпоинта, добавляющий новую запись
     *
     * @return T extends DataEntity - добавленный объект с новым ID
     */
    @PostMapping
    public T add(@Valid @RequestBody T dataEntity) {
        log.info("Попытка добавить новую запись, класс = {}", dataEntity.getClass().getSimpleName());
        try {
            validate(dataEntity);
        } catch (ValidationException ex) {
            throw new ValidationException(ex.getMessage(), ex);
        }
        T returnedEntity = dataService.create(dataEntity);
        log.info("Добавлена новая запись с id = {}: {}", returnedEntity.getId(), returnedEntity);
        return returnedEntity;
    }

    /**
     * Обработчик эндпоинта, обновляющий запись
     *
     * @return T extends DataEntity - обновленный объект
     */
    @PutMapping
    public T update(@Valid @RequestBody T dataEntity) {
        long id = dataEntity.getId();
        log.info("Попытка обновить запись с id={}, класс = {}", id, dataEntity.getClass().getSimpleName());
        try {
            validate(dataEntity);
        } catch (ValidationException ex) {
            throw new ValidationException(ex.getMessage(), ex);
        }
        dataService.update(dataEntity);
        log.info("Обновлена запись c id={}: {}", id, dataEntity);
        return dataEntity;
    }

    /**
     * Обработчик эндпоинта, получающий запись по ID
     *
     * @return T extends DataEntity - найденный объект
     */
    @GetMapping("/{id}")
    public T find(@PathVariable Long id) {
        log.info("Попытка получить запись с id={}", id);
        T data = dataService.getById(id);
        log.info("Запись с id={} получена: {}", id, data);
        return data;
    }
}
