package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        Genre genre = genreStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id: " + id + " не найден."));

        log.info("Запрос по поиску жанра с id: {} обработан. Найден жанр: {}.", id, genre);
        return genre;
    }

    public List<Genre> getAllGenre() {
        List<Genre> genres = genreStorage.getAll();

        log.info("Запрос на получение списка всех жанров обработан. Найдены жанры: {}.", genres);
        return genres;
    }
}
