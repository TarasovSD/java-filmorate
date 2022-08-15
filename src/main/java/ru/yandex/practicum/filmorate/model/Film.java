package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Film {
    @NotNull
    private long id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;
    private int countOfLikes;
    @NotNull
    private MPA mpa;
    private List<Genre> genres;

    @JsonIgnore
    private Set<Long> idsOfLikers = new HashSet<>();
}
