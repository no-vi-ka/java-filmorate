package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmLikes.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmLikesDbStorage.class, FilmDbStorage.class, UserDbStorage.class, UserMapper.class, FilmMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmLikesStorageTest {
    private final FilmLikesDbStorage filmLikesDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private static final Long TEST_FILM_ID_1 = Long.valueOf(1);
    private static final Long TEST_USER_ID = Long.valueOf(1);

    @Test
    @DisplayName("Должен добавлять лайк фильму")
    public void addLikeTest() {
        MPARating mpa = MPARating.builder().id(1).name("G").build();
        Film film = Film.builder()
                .id(TEST_FILM_ID_1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        film.setMpa(mpa);
        Film createdFilm = filmDbStorage.createFilm(film);
        User user = User.builder()
                .id(TEST_USER_ID)
                .name("test")
                .login("test")
                .email("test@test.test")
                .birthday(LocalDate.of(2000, 11, 11)).build();
        User createdUser = userDbStorage.createUser(user);
        filmLikesDbStorage.addLike(createdFilm.getId(), createdUser.getId());
        int checkLikeCounts = filmLikesDbStorage.getFilmLikes(createdFilm.getId());
        assertThat(checkLikeCounts).isEqualTo(1);
    }
}