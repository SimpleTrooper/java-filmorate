package ru.yandex.practicum.filmorate.util;

import lombok.Getter;
import lombok.Setter;

public class IdGen {
    @Getter
    @Setter
    private int id = 0;
    public int nextId() {
        return ++id;
    }
}
