package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

/**
 * Логика работы с рейтингами MPA
 */
@Service
public class MpaRatingService extends DataService<MpaRatingStorage, MpaRating> {
    @Autowired
    public MpaRatingService(@Qualifier("mpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        super(mpaRatingStorage);
    } 
}
