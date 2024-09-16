package ru.yandex.practicum.filmorate.storage.filmGenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreStorage {
    void addGenreToFilm(Film film, Set<Genre> genres);

    Set<Genre> findGenreOfFilm(Film films);

    Set<Genre> removeGenreFromFilm(Film film, List<Genre> genres);

    void removeGenreFromFilm(long id);
}
