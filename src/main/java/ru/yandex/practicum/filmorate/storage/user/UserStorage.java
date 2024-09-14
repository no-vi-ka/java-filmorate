package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    List<User> getAll();

    User create(final User user);

    User update(User user);

    Optional<User> getUser(Long id);

    User deleteUser(Long id);

    void checkUser(User userToCheck);
}
