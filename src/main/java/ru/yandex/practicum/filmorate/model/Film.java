package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likeCounts;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Set<Long> likeCounts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeCounts = new HashSet<>();
    }
}
