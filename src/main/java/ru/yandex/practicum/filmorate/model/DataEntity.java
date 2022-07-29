package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Абстрактная сущность для записей
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class DataEntity {
    private Long id; //id в теле запроса может отсутствовать (создается новая запись) - не пишем валидацию
}
