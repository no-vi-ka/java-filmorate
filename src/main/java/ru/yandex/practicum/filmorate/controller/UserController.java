package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private long idGenerator = 1;

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        log.info("Обрабатывается GET запрос от клиента.");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        log.info("Обрабатывается POST запрос от клиента.");
        // проверяем выполнение необходимых условий
        checkUser(newUser);
        log.info("Данные о пользователе (email, логин, имя пользователя, дата рождения) корректны.");
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        // формируем дополнительные данные
        newUser.setId(idGenerator++);
        // сохраняем новую публикацию в памяти приложения
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе успешно сохранена и добавлена к списку пользователей.");
        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User userToUpdate) {
        log.info("Обрабатывается PUT запрос от клиента.");
        // проверяем необходимые условия
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
        if (!userFromMap.getEmail().equals(userToUpdate.getEmail())) {
            userFromMap.setEmail(userToUpdate.getEmail());
        }
        if (!userFromMap.getLogin().equals(userToUpdate.getLogin())) {
            userFromMap.setLogin(userToUpdate.getLogin());
        }
        if (!userFromMap.getName().equals(userToUpdate.getName())) {
            if (userToUpdate.getName() == null || userToUpdate.getName().isBlank()) {
                userFromMap.setName(userToUpdate.getLogin());
            } else {
                userFromMap.setName(userToUpdate.getName());
            }
        }
        if (!userFromMap.getBirthday().equals(userToUpdate.getBirthday())) {
            userFromMap.setBirthday(userToUpdate.getBirthday());
        }
        users.put(userFromMap.getId(), userFromMap);
        log.info("Данные о пользователе успешно обновлены.");
        return userFromMap;
    }

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
