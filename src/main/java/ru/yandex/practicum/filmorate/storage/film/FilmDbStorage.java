package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final FilmMapper filmMapper;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> columns = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("films")
                .usingColumns("name", "description", "duration", "release_date", "mpa_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of("name", film.getName(),
                        "description", film.getDescription(),
                        "duration", film.getDuration(),
                        "release_date", Date.valueOf(film.getReleaseDate()),
                        "mpa_id", film.getMpa().getId()))
                .getKeys();
        return film.toBuilder().id((Long) columns.get("id")).build();
    }

    @Override
    public Film deleteFilm(Long id) {
        Film deletedFilm = getFilmById(id).get();
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return deletedFilm;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        List<Film> films = jdbcTemplate.query("SELECT f.*, mpa.id AS mpa_id, mpa.name AS mpa_name " +
                        "FROM films f " +
                        "JOIN mpa_rating mpa ON f.mpa_id = mpa.id " +
                        "WHERE f.id = ?",
                new Object[]{id},
                this::mapRowToFilm);
        return films.stream().findFirst();
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films " +
                "SET name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT f.*, mpa.name AS mpa_name " +
                "FROM films f " +
                "INNER JOIN mpa_rating AS mpa ON f.mpa_id = mpa.id", this::mapRowToFilm);
    }

    public List<Film> getMostLikedFilms(int count) {
        return jdbcTemplate.query("SELECT f.*, mpa.name AS mpa_name, COUNT(lmf.user_id) AS rating " +
                "FROM films f " +
                "INNER JOIN likes_film AS lmf ON f.id = lmf.film_id " +
                "INNER JOIN mpa_rating AS mpa ON f.mpa_id = mpa.id " +
                "GROUP BY f.id " +
                "ORDER BY rating DESC LIMIT ?", this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return filmMapper.mapRow(rs, rowNum);
    }
}