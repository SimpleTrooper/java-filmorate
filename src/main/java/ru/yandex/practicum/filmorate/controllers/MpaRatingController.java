package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

/**
 * Контроллер рейтингов MPA
 */
@RestController
@RequestMapping("/mpa")
public class MpaRatingController extends DataController<MpaRating> {
    @Autowired
    public MpaRatingController(MpaRatingService mpaRatingService) {
        super(mpaRatingService);
    }

    @Override
    protected void validate(MpaRating mpaRating) {
    }
}
