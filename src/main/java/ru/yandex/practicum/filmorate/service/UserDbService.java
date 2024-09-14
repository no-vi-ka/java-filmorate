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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbService {

    private final UserStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    public List<User> getAll() {
        List<User> usersList = userDbStorage.getAll();
        log.info("Запрос на получение всех пользователей: {}, выполнен.", usersList);
        return usersList;
    }

    public User create(User user) {
        log.info("{}", user);
        userDbStorage.checkUser(user);
        checkEmail(user);
        setNameIfNotPresent(user);
        log.info("{}", user);
        User newUser = userDbStorage.create(user);
        log.info("Добавлен новый пользователь: {}.", newUser);
        return newUser;
    }

    public User update(User user) {
        userDbStorage.checkUser(user);
        User oldUser = getUserById(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            checkEmail(user);
        }

        log.info("Пользователь обновлен: {}.", user);
        return userDbStorage.update(user);
    }

    public User getUserById(Long id) {
        User user = userDbStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден."));
        log.info("Пользователь найден: {}.", user);
        return user;
    }

    public User deleteUser(Long id) {
        User user = getUserById(id);
        log.info("Пользователь удален: {}.", user);
        return userDbStorage.deleteUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        friendshipDbStorage.addFriend(userId, friendId);
        log.info("User № {} отправил запрос в друзья User № {}.", userId, friendId);
    }

    public void endFriendship(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        friendshipDbStorage.deleteFriend(userId, friendId);
        log.info("Пользователи {} и {} больше не друзья.", user, friend);
    }

    public List<User> getFriends(Long userId) {
        getUserById(userId);
        List<User> friends = friendshipDbStorage.getUserFriends(userId);
        log.info("Запрос на получение списка друзей пользователя с id: {} выполнен. Список друзей: {}.",
                userId, friends);
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);

        List<User> commonFriends = friendshipDbStorage.getCommonFriends(userId, friendId);
        log.info("Запрос на получение общих друзей пользователя № {} и № {} выполнен.", userId, friendId);
        return commonFriends;
    }

    private void setNameIfNotPresent(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkEmail(User user) {
        boolean checkOnExist = userDbStorage.getAll().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));

        if (checkOnExist) {
            log.warn("Ошибка при выполнении запроса. Email {} занят.", user.getEmail());
            throw new ValidationException("Email " + user.getEmail() + " занят.");
        }
    }
}

//    private final UserStorage userStorage;
//    private final FriendshipStorage friendshipStorage;
//
//    public Collection<User> findAll() {
//        Collection<User> users = userStorage.findAll();for (User user : users) {
//            user.setFriendsId(friendshipStorage.getFriends(user.getId()));
//        }
//        log.info("findAll: {}", users);
//        return users;
//    }
//
//    public User createUser(User user) {
//        return userStorage.createUser(user);
////        setUserNameFromLoginIfEmpty(user);
////        log.info("create: {} - Started", user);
////        user = userStorage.createUser(user);
////        log.info("create: {} - Finished", user);
////        return user;
//    }
//
//    public User updateUser(User user) {
////        setUserNameFromLoginIfEmpty(user);
////        log.info("update: {} - Started", user);
////        user = userStorage.updateUser(user);
////        log.info("update: {} - Finished", user);
////        return user;
//        userStorage.checkContainsUserId(user.getId());
//        return userStorage.updateUser(user);
//    }
//
//    public User getUserById(Long userId) {
////        User user = userStorage.getUserById(id);
////        log.info("getUser: {} - ", user);
////        user.setFriendsId(friendshipStorage.getFriends(id));
////        return user;
//        userStorage.checkContainsUserId(userId);
//        User user = userStorage.getUserById(userId);
//        user.setFriendsId(friendshipStorage.getFriends(userId));
//        return user;
//    }
//
//    public void addFriend(Long userId, Long friendId) {
////        log.info("addFriend: {} - Started", friendId);
////        friendshipStorage.addFriend(userId, friendId);
////        log.info("addFriend: {} - Finished", userId);
////        return getUserById(userId);
//        userStorage.checkContainsUserId(userId);
//        userStorage.checkContainsUserId(friendId);
//        friendshipStorage.addFriend(userId, friendId);
//    }
//
//    public void deleteFriend(Long userId, Long friendId) {
////        log.info("deleteFriend: {} - Started", friendId);
////        friendshipStorage.deleteFriend(userId, friendId);
////        log.info("deleteFriend: {} - Finished", friendId);
//        userStorage.checkContainsUserId(userId);
//        userStorage.checkContainsUserId(friendId);
//        friendshipStorage.deleteFriend(userId, friendId);
//    }
//
//    public Collection<User> getAllFriends(Long userId) {
//        log.info("getFriends: {} - ", getUserById(userId));
////        List<User> userFriends = friendshipStorage.getAllFriends(userId);
////        return userFriends;
////    }
//
////        return getUserById(userId)
////                .getFriendsId()
////                .stream()
////                .map(this::getUserById)
////                .collect(Collectors.toList());
//
//
//        Set<Long> friendsIds = getUserById(userId).getFriendsId();
//        List<User> users = new ArrayList<>();
//        for (Long friendId : friendsIds) {
//            users.add(getUserById(friendId));
//        }
//        return users;
//    }
//
//    public Collection<User> getCommonFriends(Long userId, Long otherId) {
////        Set<Long> userFriends = getUserById(userId).getFriendsId();
////        log.info("getCommonFriends: {} - Started", userFriends);
////        Set<Long> otherFriends = getUserById(otherId).getFriendsId();
////        log.info("getCommonFriends: {} - Started", otherFriends);
////        if (userFriends == null || otherFriends == null) {
////            return Collections.emptyList();
////        }
////        Set<Long> commonFriends = new HashSet<>(userFriends);
////        commonFriends.retainAll(otherFriends);
////        log.info("getCommonFriends: {} - Finished", commonFriends);
////        return commonFriends.stream()
////                .map(userStorage::getUserById)
////                .collect(Collectors.toList());
//
//        Collection<Long> friendsId = getUserById(userId).getFriendsId();
//        Collection<Long> otherFriendsId = getUserById(otherId).getFriendsId();
//        Collection<Long> mutualFriendsId = friendsId.stream()
//                .filter(otherFriendsId::contains)
//                .toList();
//        return userStorage.findAll().stream()
//                .filter(user -> mutualFriendsId.contains(user.getId()))
//                .collect(Collectors.toList());
//    }
//
////    private void setUserNameFromLoginIfEmpty(User user) {
////        if (user.getName() == null || user.getName().isBlank()) {
////            user.setName(user.getLogin());
////        }
////    }
//}
