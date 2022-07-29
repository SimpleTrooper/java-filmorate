package ru.yandex.practicum.filmorate.model.links;

/**
 * Класс, описывающий лайки к фильму (связь фильм-пользователь)
 */
public class Like extends DataLink {
    public Like(Long filmId, Long userId) {
        super(filmId, userId);
    }

    public Long getFilmId() {
        return super.getFirstId();
    }

    public void setFilmId(Long filmId) {
        super.setFirstId(filmId);
    }

    public Long getUserId() {
        return super.getSecondId();
    }

    public void setUserId(Long userId) {
        super.setSecondId(userId);
    }
}
