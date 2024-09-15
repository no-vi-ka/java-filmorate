package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", this::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        log.info("Поступил пользователь на добавление {} ", user);
        String sqlQuery = "INSERT INTO users(name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return user.toBuilder().id(id).build();
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users " +
                "SET name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                this::mapRowToUser);
        return users.stream().findFirst();
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return userMapper.mapRow(rs, rowNum);
    }
}