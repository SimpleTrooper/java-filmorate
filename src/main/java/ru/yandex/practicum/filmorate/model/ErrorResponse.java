package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Стандартный ответ сервера для ошибок
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String error;
    private final String description;
}
