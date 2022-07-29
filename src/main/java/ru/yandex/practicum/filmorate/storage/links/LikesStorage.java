package ru.yandex.practicum.filmorate.storage.links;

import ru.yandex.practicum.filmorate.model.links.Like;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем лайков
 */
public interface LikesStorage extends DataLinkStorage<Like> {
    default List<Long> getUsersByFilmId(Long filmId) {
        return getListOfSecondIdByFirstId(filmId);
    }

    boolean removeLikesByFilmId(Long filmId);
}
