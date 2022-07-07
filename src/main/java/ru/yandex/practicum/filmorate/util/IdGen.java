package ru.yandex.practicum.filmorate.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Генератор ID
 */
public class IdGen {
    @Getter
    @Setter
    private long id = 0;
    public long nextId() {
        return ++id;
    }
}
