package ru.yandex.practicum.filmorate.storage.MPARating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("mpaDbStorage")
@RequiredArgsConstructor
public class MPARatingDbStorage implements MPARatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<MPARating> getMPAById(int id) {
        List<MPARating> mpaList = jdbcTemplate.query("SELECT * FROM mpa_rating WHERE id = ?",
                new Object[]{id},
                this::mapRowToMPA);
        return mpaList.stream().findFirst();
    }

    @Override
    public List<MPARating> getAllMPA() {
        return jdbcTemplate.query("SELECT * FROM mpa_rating ORDER BY id", this::mapRowToMPA);
    }

    private MPARating mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPARating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
