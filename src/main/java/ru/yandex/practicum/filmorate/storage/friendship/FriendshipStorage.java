package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(long id1, long id2);

    void deleteFriend(long id1, long id2);

    List<User> getUserFriends(long id);

    List<User> getCommonFriends(long id1, long id2);
}
