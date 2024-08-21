package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsId;
    private Set<Long> likedFilmsId;

    public User(long id, String email, String login, String name, LocalDate birthday, Set<Long> friendsId, Set<Long> likedFilmsId) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friendsId = new HashSet<>();
        this.likedFilmsId = new HashSet<>();
    }
}
