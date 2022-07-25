package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
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
