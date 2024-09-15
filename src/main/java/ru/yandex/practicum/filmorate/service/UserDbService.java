package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public List<User> findAll() {
        List<User> usersList = userStorage.findAll();
        log.info("Запрос на получение всех пользователей: {}, выполнен.", usersList);
        return usersList;
    }

    public User createUser(User user) {
        log.info("{}", user);
        checkEmailIsAlreadyInUse(user);
        setNameIfNotPresent(user);
        log.info("{}", user);
        User newUser = userStorage.createUser(user);
        log.info("Добавлен новый пользователь: {}.", newUser);
        return newUser;
    }

    public User updateUser(User user) {
        User oldUser = getUserById(user.getId());
        if (!oldUser.getEmail().equals(user.getEmail())) {
            checkEmailIsAlreadyInUse(user);
        }
        log.info("Пользователь обновлен: {}.", user);
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден."));
        log.info("Пользователь найден: {}.", user);
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
        log.info("User № {} отправил запрос в друзья User № {}.", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
        log.info("Пользователи {} и {} больше не друзья.", user, friend);
    }

    public List<User> getAllFriends(Long userId) {
       if (!userStorage.checkContainsUserById(userId)) {
           throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
       }
    return friendshipDbStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        if (!userStorage.checkContainsUserById(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }
        if (!userStorage.checkContainsUserById(friendId)) {
            throw new NotFoundException("Пользователь с id: " + friendId + " не найден.");
        }
            return friendshipDbStorage.getCommonFriends(userId, friendId);

    }

    private void setNameIfNotPresent(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkEmailIsAlreadyInUse(User user) {
        boolean checkOnExist = userStorage.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (checkOnExist) {
            log.warn("Ошибка при выполнении запроса. Email {} занят.", user.getEmail());
            throw new ValidationException("Email " + user.getEmail() + " занят.");
        }
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
        log.info("Пользователь с id={} удален.", id);
    }
}