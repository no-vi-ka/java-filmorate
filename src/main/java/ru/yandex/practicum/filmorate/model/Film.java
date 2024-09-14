package ru.yandex.practicum.filmorate.model;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private final long id;

    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания - 200 символов!")
    private String description;

    @NotNull(message = "Релиз фильма не может быть - null!")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть - null!")
    @Positive(message = "Продолжительность фильма должна быть больше нуля!")
    private int duration;

    @NotNull
    private MPARating mpa;

    private final Set<Genre> genres = new LinkedHashSet<>();

    @Getter
    private Set<Long> likes;

    public void addGenre(Set<Genre> genres) {
        this.genres.addAll(genres);
    }
}

