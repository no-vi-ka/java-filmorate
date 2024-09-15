package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmDbService filmDbService;

    @GetMapping
    public List<Film> findAll() {
        return filmDbService.findAll();
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmDbService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody final Film film) {
        return filmDbService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        return filmDbService.updateFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmDbService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmDbService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        return filmDbService.getMostLikedFilms(count);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmByID(@PathVariable("id") Long filmId) {
        filmDbService.deleteFilm(filmId);
    }
}