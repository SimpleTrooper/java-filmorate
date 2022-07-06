package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * Обработчик ошибок
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class ErrorHandler {
    /**
     * Обработчик исключения при отсутствии ресурса
     * @return Объект ErrorResponse - ошибка, описание ошибки. код 404
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(NotFoundException ex) {
        log.error("Ресурс не найден: {}", ex.getMessage());
        return new ErrorResponse("Ресурс не найден.", ex.getMessage());
    }

    /**
     * Обработчик исключения при неверной валидации
     * @return Объект ErrorResponse - ошибка, описание ошибки. код 400
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationHandler(ValidationException ex) {
        log.error("Ошибка валидации: {}", ex.getMessage());
        return new ErrorResponse("Ошибка валидации.", ex.getMessage());
    }

    /**
     * Обработчик остальных Runtime исключений
     * @return Объект ErrorResponse - ошибка, описание ошибки. код 500
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeHandler(RuntimeException ex) {
        log.error("Непредвиденная ошибка: {}", ex.getMessage());
        return new ErrorResponse("Непредвиденная ошибка.", ex.getMessage());
    }
}
