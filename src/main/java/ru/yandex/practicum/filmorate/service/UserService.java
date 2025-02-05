package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        if ((userId <= 0) || (friendId <= 0)) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден.");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(userId);
    }

    public void deleteFriend(long userId, long friendId) {
        if ((userId <= 0) || (friendId <= 0)) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден.");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendsId().contains(friendId)) {
            Set<Long> friendsSet1 = user.getFriendsId();
            friendsSet1.remove(friendId);
            user.setFriendsId(friendsSet1);
            Set<Long> friendsSet2 = friend.getFriendsId();
            friendsSet2.remove(userId);
            friend.setFriendsId(friendsSet2);
            userStorage.addUserToMap(user);
            userStorage.addUserToMap(friend);
        }
    }


    public Collection<User> getAllFriends(long id) {
        if (id <= 0) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
//        User user = userStorage.getUserById(id);
        List<User> friendsList = new ArrayList<>();
        Set<Long> friendsSet = userStorage.getUserById(id).getFriendsId();
        for (Long friendId : friendsSet) {
            friendsList.add(userStorage.getUserById(friendId));
        }
        return friendsList;
    }


    public Collection<User> getCommonFriends(long id, long otherId) {
        if ((id <= 0) || (otherId <= 0)) {
            throw new ValidationException("The id cannot be negative.");
        }
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        if (userStorage.getUserById(otherId) == null) {
            throw new NotFoundException("Пользователь с id = " + otherId + " не найден.");
        }
        List<User> commonFriends = new ArrayList<>();
//        User user1 = userStorage.getUserById(id);
//        User user2 = userStorage.getUserById(otherId);
        Set<Long> friendSet1 = userStorage.getUserById(id).getFriendsId();
        Set<Long> friendSet2 = userStorage.getUserById(otherId).getFriendsId();
        for (long userId : friendSet1) {
            if (friendSet2.contains(userId)) {
                commonFriends.add(userStorage.getUserById(userId));
            }
        }
        return commonFriends;
    }
}