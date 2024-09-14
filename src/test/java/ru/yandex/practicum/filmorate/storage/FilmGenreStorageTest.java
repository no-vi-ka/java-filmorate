package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({FilmDbStorage.class, FilmGenreDbStorage.class, GenreMapper.class, FilmMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmGenreStorageTest {
    private final FilmGenreDbStorage filmGenreDbStorage;
    private final FilmDbStorage filmDbStorage;
    private static final int TEST_GENRE_ID = 2;
    private static final Long TEST_FILM_ID_1 = Long.valueOf(1);

    @Test
    @DisplayName("Должен добавлять и возвращать жанры фильму.")
    public void getGenresFilmTest() {
        Genre genre = Genre.builder().id(TEST_GENRE_ID).name("Драма").build();
        Set<Genre> genresToAdd = new HashSet<>();
        genresToAdd.add(genre);
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
        film.addGenre(genresToAdd);
        filmGenreDbStorage.addGenreToFilm(created, genresToAdd);
        Map<Long, Set<Genre>> setOfFilmIdAndGenres = filmGenreDbStorage.findGenreOfFilm(List.of(created));
        int check = setOfFilmIdAndGenres.values().size();
        assertThat(genresToAdd.size()).isEqualTo(check);
    }
}