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
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmDbStorage.class, FilmMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmStorageTest {

    private static final Long TEST_FILM_ID_1 = Long.valueOf(1);
    private static final Long TEST_FILM_ID_2 = Long.valueOf(2);
    private static final int COUNT_FILMS_IN_DB = 2;

    private final FilmDbStorage filmDbStorage;

    @Test
    @DisplayName("Созданный фильм соответствует фильму из базы с равным id.")
    public void getFilmByIdTest() {
        MPARating mpa = MPARating.builder().id(1).name("G").build();
        Film film = Film.builder()
                .id(TEST_FILM_ID_1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        film.setMpa(mpa);
        Film created = filmDbStorage.createFilm(film);
        Optional<Film> filmFromDb = filmDbStorage.getFilmById(created.getId());
        assertThat(filmFromDb)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(created);
    }

    @Test
    @DisplayName("Должен возвращать все фильмы.")
    public void findAllTest() {
        MPARating mpa = MPARating.builder().id(1).name("G").build();
        Film film = Film.builder()
                .id(TEST_FILM_ID_1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        film.setMpa(mpa);
        Film created = filmDbStorage.createFilm(film);
        Film film2 = Film.builder()
                .id(TEST_FILM_ID_2)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        film2.setMpa(mpa);
        Film created2 = filmDbStorage.createFilm(film2);


        assertThat(filmDbStorage.findAll().size()).isEqualTo(COUNT_FILMS_IN_DB);
    }

    @Test
    @DisplayName("Должен обновить фильм.")
    public void updateFilmTest() {
        MPARating mpa = MPARating.builder().id(1).name("G").build();
        Film film = Film.builder()
                .id(TEST_FILM_ID_1)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .duration(200)
                .build();
        film.setMpa(mpa);
        Film created = filmDbStorage.createFilm(film);
        Film film2 = Film.builder()
                .id(TEST_FILM_ID_1)
                .name("test4")
                .description("test4")
                .releaseDate(LocalDate.of(2004, 11, 11))
                .duration(204)
                .build();
        film2.setMpa(mpa);
        Film updated = filmDbStorage.updateFilm(film2);
        Film filmFromDb = filmDbStorage.getFilmById(TEST_FILM_ID_1).get();
        assertThat(updated).usingRecursiveComparison().isEqualTo(filmFromDb);
    }
}