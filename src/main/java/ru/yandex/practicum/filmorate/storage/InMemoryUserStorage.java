package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();

    private long idGenerator = 1;

    @Override
    public Collection<User> findAll() {
        log.info("Обрабатывается GET запрос от клиента.");
        return users.values();
    }

    @Override
    public User getUserById(long id) {
        log.info("Обрабатывается GET запрос от клиента.");
        return users.get(id);
    }

    @Override
    public User createUser(@RequestBody User newUser) {
        log.info("Обрабатывается POST запрос от клиента.");
        checkUser(newUser);
        log.info("Данные о пользователе (email, логин, имя пользователя, дата рождения) корректны.");
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        newUser.setId(idGenerator++);
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе успешно сохранена и добавлена к списку пользователей.");
        return newUser;
    }

    @Override
    @PutMapping
    public User updateUser(@RequestBody User userToUpdate) {
        log.info("Обрабатывается PUT запрос от клиента.");
        checkUser(userToUpdate);
        log.info("Данные о пользователе (email, логин, имя пользователя, дата рождения) корректны.");
        if (userToUpdate.getId() <= 0) {
            log.error("Ошибка корректности id.");
            throw new ValidationException("Обновлённые данные о пользователе должны содержать положительный целочисленный Id.");
        }
        if (!users.containsKey(userToUpdate.getId())) {
            log.error("Указан несуществующий id.");
            throw new NotFoundException("Пользователь с id = " + userToUpdate.getId() + " не найден.");
        }
        User userFromMap = users.get(userToUpdate.getId());
        userFromMap.setEmail(userToUpdate.getEmail());
        userFromMap.setLogin(userToUpdate.getLogin());
        if (userToUpdate.getName() == null || userToUpdate.getName().isBlank()) {
            userFromMap.setName(userToUpdate.getLogin());
        } else {
            userFromMap.setName(userToUpdate.getName());
        }
        userFromMap.setBirthday(userToUpdate.getBirthday());
        userFromMap.setFriendsId(userToUpdate.getFriendsId());
        users.put(userFromMap.getId(), userFromMap);
        log.info("Данные о пользователе успешно обновлены.");
        return userFromMap;
    }

    @Override
    public void addUserToMap(User user) {
        users.put(user.getId(), user);
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
}
