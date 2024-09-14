package ru.yandex.practicum.filmorate.storage.MPARating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mappers.MPAMapper;

import java.sql.SQLException;
import java.util.Optional;
import java.sql.ResultSet;
import java.util.List;


@Repository("mpaDbStorage")
@RequiredArgsConstructor
public class MPARatingDbStorage implements MPARatingStorage {
    private final MPAMapper mpaMapper;
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

    private MPARating mapRowToMPA(ResultSet rs, int rowNum) throws SQLException {
        return mpaMapper.mapRow(rs, rowNum);
    }
}
