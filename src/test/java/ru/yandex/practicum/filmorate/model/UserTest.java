package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserTest {
    @Test
    public void usersShouldBeEqualsWhileDatasAreEqual() {
        User user1 = User.builder()
                .email("no.vi.ka@yandex.ru")
                .login("no-vi-ka")
                .name("no.vi.ka")
                .birthday(LocalDate.of(2000, 03, 10))
                .build();
        User user2 = User.builder()
                .email("no.vi.ka@yandex.ru")
                .login("no-vi-ka")
                .name("no.vi.ka")
                .birthday(LocalDate.of(2000, 03, 10))
                .build();
        Assertions.assertEquals(user1, user2);
    }
}
