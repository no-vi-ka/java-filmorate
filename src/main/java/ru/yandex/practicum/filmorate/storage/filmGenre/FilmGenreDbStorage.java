package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository("filmGenreDbStorage")
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenreToFilm(Film film, Set<Genre> genres) {

        if (genres == null) {
            return;
        }

        String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (? , ?)";

        List<Genre> listOfGenre = new ArrayList<>(genres);

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setInt(2, listOfGenre.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public Map<Long, Set<Genre>> findGenreOfFilm(List<Film> films) {

        Map<Long, Set<Genre>> genres = new HashMap<>();

        List<Long> filmIds = films.stream().map(Film::getId).collect(Collectors.toList());

        String placeholders = String.join(",", Collections.nCopies(filmIds.size(), "?"));

        String sqlQuery = String.format("SELECT fg.film_id, fg.genre_id, g.name " +
                "FROM film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE film_id IN (%s) " +
                "ORDER BY fg.film_id, fg.genre_id", placeholders);

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, filmIds.toArray());

        while (rs.next()) {
            long filmID = rs.getInt("film_id");

            Genre genre = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("name"))
                    .build();

            genres.computeIfAbsent(filmID, k -> new LinkedHashSet<>()).add(genre);
        }

        return genres;
    }

    @Override
    public void removeGenreFromFilm(long id) {

        String sqlQuery = "DELETE FROM film_genres " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Set<Genre> removeGenreFromFilm(Film film, List<Genre> genres) {

        if (genres == null) {
            return new LinkedHashSet<>();
        }

        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?, genre_id = ?)";

        List<Genre> listOfGenre = new ArrayList<>(genres);

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setInt(2, listOfGenre.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
        return new LinkedHashSet<>(genres);
    }
}
