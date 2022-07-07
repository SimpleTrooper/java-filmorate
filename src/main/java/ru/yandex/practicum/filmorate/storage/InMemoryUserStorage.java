package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Реализация интерфейса для работы с хранилищем пользователей. Хранение в памяти
 */
@Component
public class InMemoryUserStorage extends InMemoryDataStorage<User> implements UserStorage {
}
