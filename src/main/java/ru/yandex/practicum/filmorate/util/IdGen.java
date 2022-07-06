package ru.yandex.practicum.filmorate.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Генератор ID
 */
public class IdGen {
    @Getter
    @Setter
    private int id = 0;
    public int nextId() {
        return ++id;
    }
}
