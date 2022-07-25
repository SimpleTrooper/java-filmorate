package ru.yandex.practicum.filmorate.storage.inmemimpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * Реализация интерфейса для работы с хранилищем пользователей. Хранение в памяти
 */
@Component
public class InMemoryUserStorage extends InMemoryDataStorage<User> implements UserStorage {
}
