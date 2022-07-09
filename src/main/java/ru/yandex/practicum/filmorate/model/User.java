package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    private long id;
    @NotBlank
    @Email
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String login;
    @NotNull
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
}
