package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

@Repository("genresDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final GenreMapper genreMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres WHERE id = ?",
                new Object[]{id},
                this::mapRowToGenre);
        return genres.stream().findFirst();
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return genreMapper.mapRow(rs, rowNum);
    }
}

