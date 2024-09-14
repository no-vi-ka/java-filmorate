package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;
import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> getGenreById(int id);
}
