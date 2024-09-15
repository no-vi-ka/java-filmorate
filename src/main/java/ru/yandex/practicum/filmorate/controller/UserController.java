package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDbService userDbService;

    @GetMapping
    public List<User> findAll() {
        return userDbService.findAll();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Long userId) {
        return userDbService.getUserById(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        return userDbService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userDbService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userDbService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userDbService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        return userDbService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userDbService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserByID(@PathVariable Long userId) {
        userDbService.deleteUser(userId);
    }
}