package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс пользователя
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends DataEntity {
    @NotBlank(message = "E-mail не может быть пустым")
    @Email(message = "Неверный формат E-mail")
    private String email;
    @NotBlank(message = "login не может быть пустым")
    @Pattern(regexp = "[^\\s]+", message = "login не должен содержать пробелы")
    private String login;
    private String name; //Ручная валидации при пустом имени (подстановка login)
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();//id друзей

    @Builder
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriends(Collection<Long> friends) {
        this.friends.addAll(friends);
    }

    public void addFriend(Long userId) {
        friends.add(userId);
    }

    public void removeFriend(Long userId) {
        friends.remove(userId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("login", login);
        map.put("name", name);
        map.put("birthday", birthday);
        return map;
    }
}
