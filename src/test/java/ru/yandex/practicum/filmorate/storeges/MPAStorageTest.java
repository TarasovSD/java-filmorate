package ru.yandex.practicum.filmorate.storeges;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPAStorageTest {

    private final MPAStorage mpaStorage;

    @Test
    public void testMPAStorage() {
        Optional<MPA> mpaOptionalId1 = Optional.ofNullable(mpaStorage.getMPAById(1));

        assertThat(mpaOptionalId1)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(mpaOptionalId1)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );

        Optional<MPA> mpaOptionalId2 = Optional.ofNullable(mpaStorage.getMPAById(2));

        assertThat(mpaOptionalId2)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 2)
                );

        assertThat(mpaOptionalId2)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG")
                );

        Optional<MPA> mpaOptionalId3 = Optional.ofNullable(mpaStorage.getMPAById(3));

        assertThat(mpaOptionalId3)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 3)
                );

        assertThat(mpaOptionalId3)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13")
                );

        Optional<MPA> mpaOptionalId4 = Optional.ofNullable(mpaStorage.getMPAById(4));

        assertThat(mpaOptionalId4)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 4)
                );

        assertThat(mpaOptionalId4)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "R")
                );

        Optional<MPA> mpaOptionalId5 = Optional.ofNullable(mpaStorage.getMPAById(5));

        assertThat(mpaOptionalId5)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 5)
                );

        assertThat(mpaOptionalId5)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "NC-17")
                );

        Optional<List<MPA>> allMPAsOptional = Optional.ofNullable(mpaStorage.getMPAs());

        assertThat(allMPAsOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.size()).isEqualTo(5)
                );

        assertThat(allMPAsOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.get(0)).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(allMPAsOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.get(0)).hasFieldOrPropertyWithValue("name", "G")
                );

        assertThat(allMPAsOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.get(4)).hasFieldOrPropertyWithValue("id", 5)
                );

        assertThat(allMPAsOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa.get(4)).hasFieldOrPropertyWithValue("name", "NC-17")
                );
    }
}
