package ru.yandex.practicum.filmorate.model.links;

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
        return super.getFirstId();
    }

    public void setGenreId(Long filmId) {
        super.setFirstId(filmId);
    }
}
