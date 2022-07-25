package ru.yandex.practicum.filmorate.model.links;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship extends DataLink {
    private boolean status;

    public Friendship(Long userId, Long friendId, boolean status) {
        super(userId, friendId);
        this.status = status;
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
