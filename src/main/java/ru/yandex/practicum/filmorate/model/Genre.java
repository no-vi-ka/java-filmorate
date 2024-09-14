package ru.yandex.practicum.filmorate.model;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Data
@Builder(toBuilder = true)
public class Genre {
    @Positive
    private final int id;
    @NotBlank
    private String name;
}

