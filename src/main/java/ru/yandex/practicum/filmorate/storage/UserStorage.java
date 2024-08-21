package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> findAll();

    public User getUserById(long id);

    public User createUser(User newUser);

    public User updateUser(User userToUpdate);

    public void addUserToMap(User user);

    public void checkUser(User userToCheck);

}
