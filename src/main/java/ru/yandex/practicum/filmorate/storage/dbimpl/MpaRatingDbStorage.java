package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Реализация хранилища рейтингов MPA в БД
 */
@Component("mpaRatingDbStorage")
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating add(MpaRating mpaRating) {
        if (mpaRating == null) {
            return null;
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpa_ratings")
                .usingGeneratedKeyColumns("mpa_rating_id");
        Long mpaRatingId = simpleJdbcInsert.executeAndReturnKey(mpaRating.toMap()).longValue();
        mpaRating.setId(mpaRatingId);
        return mpaRating;
    }

    @Override
    public MpaRating update(MpaRating mpaRating) {
        if (mpaRating == null) {
            return null;
        }
        if (!contains(mpaRating.getId())) {
            throw new NotFoundException(String.format("Не найден рейтинг MPA с id = %d", mpaRating.getId()));
        }
        String sql = "UPDATE mpa_ratings SET name = ? WHERE mpa_rating_id = ?";
        jdbcTemplate.update(sql, mpaRating.getName(), mpaRating.getId());
        return mpaRating;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT mpa_rating_id, name FROM mpa_ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpaRating(rs));
    }

    @Override
    public MpaRating findById(Long id) {
        String sql = "SELECT mpa_rating_id, name FROM mpa_ratings WHERE mpa_rating_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpaRating(rs), id);
    }

    @Override
    public boolean contains(Long mpaRatingId) {
        try {
            findById(mpaRatingId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return false;
        }
        return true;
    }

    private MpaRating makeMpaRating(ResultSet resultSet) throws SQLException {
        return new MpaRating(resultSet.getLong("mpa_rating_id"), resultSet.getString("name"));
    }
}
