package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
public class MpaRating extends DataEntity {
    @NotBlank
    private String name;

    public MpaRating(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        return map;
    }
}
