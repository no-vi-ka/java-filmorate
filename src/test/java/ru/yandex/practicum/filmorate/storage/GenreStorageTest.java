package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreDbStorage.class, GenreMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreStorageTest {
    private final GenreDbStorage genreDbStorage;
    private static final int TEST_GENRE_ID = 2;

    @Test
    @DisplayName("Должен возвращать все жанры.")
    public void getAllGenresTest() {
        int coutGenres = genreDbStorage.getAll().size();
        assertThat(coutGenres).isEqualTo(6);
    }

    @Test
    @DisplayName("Должен вернуть соответствующий id жанр.")
    public void getGenreById() {
        Genre genre = Genre.builder().id(TEST_GENRE_ID).name("Драма").build();
        Optional<Genre> genreFromDb = genreDbStorage.getGenreById(TEST_GENRE_ID);
        assertThat(genreFromDb)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }
}
