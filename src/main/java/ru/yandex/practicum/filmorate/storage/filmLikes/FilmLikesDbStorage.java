package ru.yandex.practicum.filmorate.storage.filmLikes;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.Set;

@Repository("LikesFilmDbStorage")
@RequiredArgsConstructor
public class FilmLikesDbStorage implements FilmLikesStorage {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQueryForLikeMark = "INSERT INTO likes_film (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryForLikeMark, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlQueryForUnLikeMark = "DELETE FROM likes_film WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQueryForUnLikeMark, filmId, userId);
    }

    @Override
    public int getFilmLikes(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("FILM_ID", id);
        String sqlQuery = "SELECT COUNT(USER_ID) FROM likes_film WHERE FILM_ID = :FILM_ID";
        Set<Long> filmLikesUserIds = new HashSet<>(jdbc.queryForList(sqlQuery, sqlParameterSource, Long.class));
        return filmLikesUserIds.size();
    }
}