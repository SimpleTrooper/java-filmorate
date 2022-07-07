package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

/**
 * Обработчик ошибок
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class ErrorHandler {
    /**
     * Обработчик исключения при отсутствии ресурса
     *
     * @return описание ошибки. код 404
     */
    @ExceptionHandler
    public ResponseEntity<String> notFoundHandler(NotFoundException ex) {
        String errorMessage = String.format("Ресурс не найден: %s", ex.getMessage());
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик исключения при неверной валидации
     *
     * @return описание ошибки. код 400
     */
    @ExceptionHandler
    public ResponseEntity<String> validationHandler(ValidationException ex) {
        String errorMessage = String.format("Ошибка валидации: %s", ex.getMessage());
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик остальных Runtime исключений
     *
     * @return описание ошибки. код 500
     */
    @ExceptionHandler
    public ResponseEntity<String> runtimeHandler(RuntimeException ex) {
        String errorMessage = String.format("Непредвиденная ошибка: %s", ex.getMessage());
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
