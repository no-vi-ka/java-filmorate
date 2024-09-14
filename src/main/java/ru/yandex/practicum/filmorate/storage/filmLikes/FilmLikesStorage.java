package ru.yandex.practicum.filmorate.storage.filmLikes;

public interface FilmLikesStorage {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    int getFilmLikes(Long id);
}