package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User getUserById(long id);

    User createUser(User newUser);

    User updateUser(User userToUpdate);

    void addUserToMap(User user);

    void checkUser(User userToCheck);
}
