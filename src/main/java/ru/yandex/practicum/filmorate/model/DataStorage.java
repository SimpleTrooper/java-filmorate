package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public abstract class DataStorage {
    private int id; //id в теле запроса может отсутствовать (создается новая запись) - не пишем валидацию
}
