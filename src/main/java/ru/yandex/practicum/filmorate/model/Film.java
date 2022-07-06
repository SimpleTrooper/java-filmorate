package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс фильма
 */
@Data
public class Film extends DataEntity {
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    private LocalDate releaseDate;//Ручная валидация даты релиза
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration; //продолжительность в минутах
    private final Set<Integer> userLikes = new HashSet<>();//id пользователей, поставивших лайк

    public void addLike(Integer userId) {
        userLikes.add(userId);
    }

    public void removeLike(Integer userId) {
        userLikes.remove(userId);
    }

    public int getLikesCount() {
        return userLikes.size();
    }
}
