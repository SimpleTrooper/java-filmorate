package ru.yandex.practicum.filmorate.model.links;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, определеяющий дружбу между двумя пользователями и её статус (связь пользователь-пользователь)
 */
@Getter
@Setter
public class Friendship extends DataLink {
    private boolean status;

    public Friendship(Long userId, Long friendId, boolean status) {
        super(userId, friendId);
        this.status = status;
    }

    public Friendship(Long userId, Long friendId) {
        super(userId, friendId);
    }

    public Long getUserId() {
        return super.getFirstId();
    }

    public void setUserId(Long userId) {
        super.setFirstId(userId);
    }

    public Long getFriendId() {
        return super.getSecondId();
    }

    public void setFriendId(Long friendId) {
        super.setSecondId(friendId);
    }
}
