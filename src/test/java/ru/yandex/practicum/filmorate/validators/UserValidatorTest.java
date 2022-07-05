package ru.yandex.practicum.filmorate.validators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;



public class UserValidatorTest {
    public static User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
    }

    /*
    Стандартное поведение при не пустом имени
     */
    @Test
    public void shouldUseNonEmptyName() {
        String name = "name";
        user.setName(name);
        UserValidator.validateAndSetName(user);
        String actualName = user.getName();
        assertEquals(actualName, name, "Изменяется имя, если оно не пустое");
    }

    /*
    Поведение при пустом имени
     */
    @Test
    public void shouldUseLoginWhenNameIsEmpty() {
        user.setName("");
        UserValidator.validateAndSetName(user);
        String actualName = user.getName();
        assertEquals(actualName, user.getLogin(), "Не используется login при пустом имени");
    }

    /*
    Поведение при name = null
     */
    @Test
    public void shouldUseLoginWhenNameIsNull() {
        user.setName(null);
        UserValidator.validateAndSetName(user);
        String actualName = user.getName();
        assertEquals(actualName, user.getLogin(), "Не используется login при имени = null");
    }
}
