package ru.yandex.practicum.filmorate.model.links;

/**
 * Класс связи фильм-жанр
 */
public class FilmGenre extends DataLink {
    public FilmGenre(Long filmId, Long genreId) {
        super(filmId, genreId);
    }

    public Long getFilmId() {
        return super.getFirstId();
    }

    public void setFilmId(Long filmId) {
        super.setFirstId(filmId);
    }

    public Long getGenreId() {
        return super.getSecondId();
    }

    public void setGenreId(Long genreId) {
        super.setSecondId(genreId);
    }
}
