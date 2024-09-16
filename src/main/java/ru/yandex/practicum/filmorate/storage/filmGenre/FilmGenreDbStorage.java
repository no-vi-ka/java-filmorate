package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.stream.Collectors;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Repository("filmGenreDbStorage")
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final GenreStorage genreStorage;
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
    public Set<Genre> findGenreOfFilm(Film film) {
        Set<Genre> toReturn = new LinkedHashSet<>();
        Map<Integer, Genre> genreMap = getAllGenreMap();
        Long filmId = film.getId();
        String sqlQuery = "SELECT genre_id FROM film_genres WHERE film_id = ?";
        List<Integer> filmGenresId = jdbcTemplate.queryForList(sqlQuery, new Object[]{filmId}, Integer.class);
        for (Integer genreId : filmGenresId) {
            toReturn.add(genreMap.get(genreId));
        }
        return toReturn;
    }

    @Override
    public List<Film> findGenreOfFilms(List<Film> films) {
        Map<Integer, Genre> genreMap = getAllGenreMap();
        String sql = "SELECT film_id FROM films";
        List<Long> filmsIds = jdbcTemplate.queryForList(sql, Long.class);
        String sql1 = "SELECT film_id, genre_id FROM film_genres GROUP BY film_id";
        HashMap<Long, List<Integer>> connect = jdbcTemplate.queryForObject(sql1, HashMap.class);
        Set<Genre> genresToAdd = new HashSet<>();
        for (Film film : films) {
            Long filmId = film.getId();
            genresToAdd.clear();
            List<Integer> genresIdForFilm = connect.get(filmId);
            for (Integer genreId : genresIdForFilm) {
                Genre genreToAdd = genreMap.get(genreId);
                genresToAdd.add(genreToAdd);
            }
            film.addGenre(genresToAdd);
        }
        return films;
    }

    @Override
    public Map<Integer, Genre> getAllGenreMap() {
        List<Genre> allGenres = genreStorage.getAll();
        Map<Integer, Genre> genreMap = new HashMap<>();
        for (Genre genre : allGenres) {
            genreMap.put(genre.getId(), genre);
        }
        return genreMap;
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
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ? AND genre_id = ?)";
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