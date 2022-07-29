package ru.yandex.practicum.filmorate.model.links;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Абстрактная сущность для связи двух таблиц
 */
@Data
@AllArgsConstructor
public abstract class DataLink {
    @NotNull
    private Long firstId;
    @NotNull
    private Long secondId;
}
