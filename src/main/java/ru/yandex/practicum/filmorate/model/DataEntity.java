package ru.yandex.practicum.filmorate.model;

import lombok.Data;

/**
 * Абстрактная сущность для записей
 */
@Data
public abstract class DataEntity {
    private Long id; //id в теле запроса может отсутствовать (создается новая запись) - не пишем валидацию
}
