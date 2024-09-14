package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDbService userDbService;

    @GetMapping
    public List<User> findAll() {
        return userDbService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userDbService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userDbService.update(user);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        return userDbService.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public User deleteUserByID(@PathVariable("id") Long userId) {
        return userDbService.deleteUser(userId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void putFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userDbService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId) {
        userDbService.endFriendship(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long userId) {
        return userDbService.getFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable("id") Long userId,
            @PathVariable("otherId") Long secondUserId) {
        return userDbService.getCommonFriends(userId, secondUserId);
    }
}
