package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", this::mapRowToUser);
    }

    @Override
    public User create(User user) {
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
    public User update(User user) {
        String sqlQuery = "UPDATE users " +
                "SET name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());

        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                this::mapRowToUser);
        return users.stream().findFirst();
    }

    @Override
    public User deleteUser(Long id) {
        User user = getUser(id).get();
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public void checkUser(User userToCheck) {
        if (userToCheck.getEmail() == null || userToCheck.getEmail().isBlank() || !userToCheck.getEmail().contains("@")) {
            log.error("Ошибка: введён некорректный email.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (userToCheck.getLogin() == null || userToCheck.getLogin().isBlank() || userToCheck.getLogin().contains(" ")) {
            log.error("Ошибка: введён некорректный логин.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка: введён некорректный день рождения.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
//    private final JdbcTemplate jdbcTemplate;
//
//@Override
//    public List<User> findAll() {
//        String sql = "SELECT * FROM USERS " +
//                "GROUP BY USER_ID";
//
//        return jdbcTemplate.query(sql, new UserMapper());
//    }
//
//    @Override
//    public User createUser(User user) {
//        checkUser(user);
//        String sql = "INSERT INTO USERS (USER_NAME, EMAIL, LOGIN, BIRTHDAY) " +
//                "VALUES (?, ?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
//            stmt.setString(1, user.getName());
//            stmt.setString(2, user.getEmail());
//            stmt.setString(3, user.getLogin());
//            stmt.setDate(4, Date.valueOf(user.getBirthday()));
//            return stmt;
//        }, keyHolder);
//
//        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
//        user.setId(id);
//
//        return user;
//    }
//
//    @Override
//    public User updateUser(User user) {
//        checkContainsUserId(user.getId());
//        String sql = "UPDATE USERS SET USER_NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? "
//                + "WHERE USER_ID = ?";
//        jdbcTemplate.update(sql,
//                user.getName(),
//                user.getEmail(),
//                user.getLogin(),
//                Date.valueOf(user.getBirthday()),
//                user.getId());
//        return user;
//    }
//
//@Override
//    public User getUserById(Long id) {
//        if (id <= 0) {
//            throw new ValidationException("The id cannot be negative.");
//        }
//        checkContainsUserId(id);
//        String sql = "SELECT U.USER_ID, U.USER_NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY " +
//                "FROM USERS AS U " +
//                "WHERE U.USER_ID = ? " +
//                "GROUP BY U.USER_ID";
//        return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
//    }
//
//    @Override
//    public void checkUser(User userToCheck) {
//        if (userToCheck.getEmail() == null || userToCheck.getEmail().isBlank() || !userToCheck.getEmail().contains("@")) {
//            log.error("Ошибка: введён некорректный email.");
//            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
//        }
//        if (userToCheck.getLogin() == null || userToCheck.getLogin().isBlank() || userToCheck.getLogin().contains(" ")) {
//            log.error("Ошибка: введён некорректный логин.");
//            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
//        }
//        if (userToCheck.getBirthday().isAfter(LocalDate.now())) {
//            log.error("Ошибка: введён некорректный день рождения.");
//            throw new ValidationException("Дата рождения не может быть в будущем.");
//        }
//    }
//
//    @Override
//    public void checkContainsUserId(Long id) {
//        String sql = "SELECT * FROM USERS " +
//                "WHERE USER_ID = ?";
//        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);
//
//        if (!rows.next()) {
//            throw new NotFoundException("Пользователь с id= " + id + " не найден!");
//        }
//    }
}
