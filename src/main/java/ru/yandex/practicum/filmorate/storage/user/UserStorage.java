package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User createUser(final User user);

    User updateUser(User user);

    Optional<User> getUserById(Long id);

    User deleteUser(Long id);
}
