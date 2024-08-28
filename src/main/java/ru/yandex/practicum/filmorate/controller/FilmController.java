package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmStorage.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody Film newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film filmToUpdate) {
        return filmStorage.updateFilm(filmToUpdate);
    }

    @PutMapping("/{filmId}/like/{id}")
    public void addLike(@PathVariable long filmId, @PathVariable long id) {
        filmService.addLike(filmId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostLikedFilms(@RequestParam(name = "count",
            defaultValue = "10", required = false) int count) {
        return filmService.getMostLikedFilms(count);
    }
}