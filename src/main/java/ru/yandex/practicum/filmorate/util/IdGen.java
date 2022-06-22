package ru.yandex.practicum.filmorate.util;

public class IdGen {
    private int id = 0;
    public int nextId() {
        return ++id;
    }
}
