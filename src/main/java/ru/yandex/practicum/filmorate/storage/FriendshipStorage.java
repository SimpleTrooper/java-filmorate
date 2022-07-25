package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.links.Friendship;

import java.util.List;

public interface FriendshipStorage extends DataLinkStorage<Friendship> {

    default List<Long> getFriendsByUserId(Long userId) {
        return getListOfSecondIdByFirstId(userId);
    }

    void setFriendshipStatusConfirmed(Friendship friendship);

    void setFriendshipStatusNotConfirmed(Friendship friendship);
}
