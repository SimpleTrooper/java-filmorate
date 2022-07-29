package ru.yandex.practicum.filmorate.storage.links;

import ru.yandex.practicum.filmorate.model.links.Friendship;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем дружбы пользователей
 */
public interface FriendshipStorage extends DataLinkStorage<Friendship> {

    default List<Long> getFriendsByUserId(Long userId) {
        return getListOfSecondIdByFirstId(userId);
    }

    boolean setFriendshipStatusConfirmed(Friendship friendship);

    boolean setFriendshipStatusNotConfirmed(Friendship friendship);
}
