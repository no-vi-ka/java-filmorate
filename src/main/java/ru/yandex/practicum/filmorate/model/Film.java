package ru.yandex.practicum.filmorate.model;

import lombok.*;
import jakarta.validation.constraints.*;

import java.util.LinkedHashSet;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private final long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
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

