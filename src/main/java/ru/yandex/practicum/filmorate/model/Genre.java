package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс жанра фильмов
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Genre extends DataEntity {
    @NotBlank
    private String name;

    public Genre(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        return map;
    }
}
