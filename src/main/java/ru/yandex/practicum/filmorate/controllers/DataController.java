package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.DataStorage;
import ru.yandex.practicum.filmorate.util.IdGen;

@Slf4j
@RestController
public abstract class DataController<T extends DataStorage> {
    private final IdGen idGen = new IdGen();
    private final Map<Integer, T> data = new LinkedHashMap<>();

    protected abstract void validate(T dataStorage);

    @GetMapping
    public List<DataStorage> getAll() {
        return new ArrayList<>(data.values());
    }

    @PostMapping
    public DataStorage add(@Valid @RequestBody T dataStorage) {
        try {
            validate(dataStorage);
        } catch (ValidationException ex) {
            log.error("Попытка добавить запись. " + ex.getMessage());
            throw new ValidationException(ex.getMessage(), ex);
        }
        int nextFilmId = idGen.nextId();
        dataStorage.setId(nextFilmId);
        data.put(nextFilmId, dataStorage);
        log.info("Добавлена новая запись: {}", dataStorage);
        return dataStorage;
    }

    @PutMapping
    public DataStorage update(@Valid @RequestBody T dataStorage) {
        int id = dataStorage.getId();
        if (!data.containsKey(id)) {
            log.error("Попытка обновить запись с несуществующим id={}", id);
            throw new NotFoundException("Запись с id = " + id + " не найдена");
        }
        try {
            validate(dataStorage);
        } catch (ValidationException ex) {
            log.error("Попытка обновить запись с id={}. " + ex.getMessage(), id);
            throw new ValidationException(ex.getMessage(), ex);
        }
        dataStorage.setId(id);
        data.put(id, dataStorage);
        log.info("Обновлена запись c id={}: {}",id, dataStorage);
        return dataStorage;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validationHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error("Попытка обновить/добавить запись. Ошибка валидации - " + fieldName + ": " + errorMessage);
        });
        return errors;
    }
}
