package ru.yandex.practicum.filmorate.storage.filmLikes;

public interface FilmLikesStorage {
    void addLikeFilm(long filmId, long userId);

    void deleteLikeFilm(long filmId, long userId);
}