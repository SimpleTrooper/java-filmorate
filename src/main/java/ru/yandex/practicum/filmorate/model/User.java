package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс пользователя
 */
@Data
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

    public void addFriend(Long userId) {
        friends.add(userId);
    }

    public void removeFriend(Long userId) {
        friends.remove(userId);
    }
}
