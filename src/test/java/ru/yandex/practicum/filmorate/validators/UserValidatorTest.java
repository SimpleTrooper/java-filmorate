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
    Стандартное поведение валидации email
     */
    @Test
    public void shouldValidateGoodEmail() {
        assertDoesNotThrow(() -> UserValidator.validateEmail(user),
                "Выбрасывается исключение при правильном email");
    }

    /*
    Поведение при пустом email
     */
    @Test
    public void shouldNotValidateEmptyEmail() {
        user.setEmail("");
        assertThrows(UserValidationException.class, () -> UserValidator.validateEmail(user),
                "Не выбрасывается исключение при пустом email");
    }

    /*
    Поведение при email = null
     */
    @Test
    public void shouldNotValidateNullEmail() {
        user.setEmail(null);
        assertThrows(UserValidationException.class, () -> UserValidator.validateEmail(user),
                "Не выбрасывается исключение при email = null");
    }

    @Test
    /*
    Поведение при не содержащем символ @ email
     */
    public void shouldNotValidateEmailWithoutSpecialSymbol() {
        user.setEmail("email");
        assertThrows(UserValidationException.class, () -> UserValidator.validateEmail(user),
                "Не выбрасывается исключение при не содержащем @ email");
    }

    /*
   Стандартное поведение валидации login
    */
    @Test
    public void shouldValidateGoodLogin() {
        assertDoesNotThrow(() -> UserValidator.validateLogin(user),
                "Выбрасывается исключение при правильном login");
    }

    /*
    Поведение при пустом login
     */
    @Test
    public void shouldNotValidateEmptyLogin() {
        user.setLogin("");
        assertThrows(UserValidationException.class, () -> UserValidator.validateLogin(user),
                "Не выбрасывается исключение при пустом login");
    }

    /*
    Поведение при login = null
     */
    @Test
    public void shouldNotValidateNullLogin() {
        user.setLogin(null);
        assertThrows(UserValidationException.class, () -> UserValidator.validateLogin(user),
                "Не выбрасывается исключение при login = null");
    }


    /*
    Поведение при содержащем пробелы login
     */
    @Test
    public void shouldNotValidateLoginWithSpaces() {
        user.setLogin("l ogin");
        assertThrows(UserValidationException.class, () -> UserValidator.validateLogin(user),
                "Не выбрасывается исключение при содержащем пробелы login");
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

    /*
    Стандартное поведение при дне рождения = сегодня
     */
    @Test
    public void shouldValidateWhenBirthdayIsToday() {
        assertDoesNotThrow(() -> UserValidator.validateBirthday(user),
                "Выбрасывается исключение при дате рождения = сегодня");
    }

    /*
    Поведение при дне рождения в будущем
     */
    @Test
    public void shouldNotValidateWhenBirthdayIsInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(UserValidationException.class, () -> UserValidator.validateBirthday(user),
                "Не выбрасывается исключение при дне рождения в будущем");
    }
}
