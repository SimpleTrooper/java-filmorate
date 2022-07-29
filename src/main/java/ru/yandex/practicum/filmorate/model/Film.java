package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс фильма
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Film extends DataEntity {
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    private LocalDate releaseDate;//Ручная валидация даты релиза
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration; //продолжительность в минутах

    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<Genre> genres = new LinkedHashSet<>();//жанры фильма
    //важен порядок id жанров для прохождения одного из тестов

    @NotNull
    private MpaRating mpa;//MPA-рейтинг фильма
    private final Set<Long> userLikes = new HashSet<>();//id пользователей, поставивших лайк

    @Builder
    public Film (Long id, String name, String description, LocalDate releaseDate, int duration,  MpaRating mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addLike(Long userId) {
        userLikes.add(userId);
    }

    public void addAllLikes(Collection<Long> likes) {
        userLikes.addAll(likes);
    }

    public void removeLike(Long userId) {
        userLikes.remove(userId);
    }

    public int getLikesCount() {
        return userLikes.size();
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addAllGenres(Collection<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("release_date", Date.valueOf(releaseDate));
        map.put("duration", duration);
        Long mpaRatingId = null;
        if (mpa != null) {
            mpaRatingId = mpa.getId();
        }
        map.put("mpa_rating_id", mpaRatingId);
        return map;
    }
}
