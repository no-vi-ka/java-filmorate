package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final Map<Long, Film> films = new HashMap<>();

    private long idGenerator = 1;

    private final LocalDate theEarliestFilmDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Обрабатывается GET запрос от клиента.");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film newFilm) {
        log.info("Обрабатывается POST запрос от клиента.");
        // проверяем выполнение необходимых условий
        checkFilm(newFilm);
        log.info("Данные о фильме (название, описание, дата выхода, продолжительность) корректны.");
        // формируем дополнительные данные
        newFilm.setId(idGenerator++);
        // сохраняем новую публикацию в памяти приложения
        films.put(newFilm.getId(), newFilm);
        log.info("Информация о фильме успешно сохранена и добавлена к списку фильмов.");
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film filmToUpdate) {
        log.info("Обрабатывается PUT запрос от клиента.");
        // проверяем необходимые условия
        checkFilm(filmToUpdate);
        log.info("Данные о фильме (название, описание, дата выхода, продолжительность) корректны.");
        if (filmToUpdate.getId() <= 0) {
            log.error("Ошибка корректности id.");
            throw new ValidationException("Обновлённые данные о фильме должны содержать положительный целочисленный Id.");
        }
        if (!films.containsKey(filmToUpdate.getId())) {
            log.error("Указан несуществующий id.");
            throw new NotFoundException("Пост с id = " + filmToUpdate.getId() + " не найден.");
        }
        Film filmFromMap = films.get(filmToUpdate.getId());
        if (!filmFromMap.getName().equals(filmToUpdate.getName())) {
            filmFromMap.setName(filmToUpdate.getName());
        }
        if (!filmFromMap.getDescription().equals(filmToUpdate.getDescription())) {
            filmFromMap.setDescription(filmToUpdate.getDescription());
        }
        if (!filmFromMap.getReleaseDate().equals(filmToUpdate.getReleaseDate())) {
            filmFromMap.setReleaseDate(filmToUpdate.getReleaseDate());
        }
        if (filmFromMap.getDuration() != filmToUpdate.getDuration()) {
            filmFromMap.setDuration(filmToUpdate.getDuration());
        }
        films.put(filmFromMap.getId(), filmFromMap);
        log.info("Данные о фильме успешно обновлены.");
        return filmFromMap;
    }

    public void checkFilm(Film filmToCheck) {
        if (filmToCheck.getName() == null || filmToCheck.getName().isBlank()) {
            log.error("Ошибка: введено некорректное название фильма.");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (filmToCheck.getDescription().length() > 200) {
            log.error("Ошибка: введено описание фильма некорректной длины.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (filmToCheck.getReleaseDate().isBefore(theEarliestFilmDate)) {
            log.error("Ошибка: введена слишком ранняя дата выхода фильма.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (filmToCheck.getDuration() <= 0) {
            log.error("Ошибка: введена некорректная продолжительность фильма.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
