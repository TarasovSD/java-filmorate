package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
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

//    @JsonIgnore
//    private Set<User> friends = new HashSet<>();

//    public User(long user_id, String user_email, String login, String name, LocalDate birthday) {
//    }
}
