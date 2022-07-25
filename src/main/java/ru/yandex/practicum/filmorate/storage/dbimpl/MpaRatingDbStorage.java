package ru.yandex.practicum.filmorate.storage.dbimpl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaRatingDbStorage")
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating add(MpaRating mpaRating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpa_ratings")
                .usingGeneratedKeyColumns("mpa_rating_id");
        Long mpaRatingId = simpleJdbcInsert.executeAndReturnKey(mpaRating.toMap()).longValue();
        return findById(mpaRatingId);
    }

    @Override
    public MpaRating update(MpaRating mpaRating) {
        String sql = "UPDATE mpa_ratings SET name = ? WHERE mpa_rating_id = ?";
        jdbcTemplate.update(sql, mpaRating.getName(), mpaRating.getId());
        return findById(mpaRating.getId());
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

    private MpaRating makeMpaRating(ResultSet resultSet) throws SQLException {
        return new MpaRating(resultSet.getLong("mpa_rating_id"), resultSet.getString("name"));
    }
}
