package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    private final long id;
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    @Past
    @NotNull
    private LocalDate birthday;
}
