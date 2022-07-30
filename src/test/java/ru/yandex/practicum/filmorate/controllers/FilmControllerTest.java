package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FilmController.class)
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    @MockBean
    private FilmService filmService;
    @Autowired
    @MockBean
    private FilmStorage filmStorage;
    @MockBean
    private Film film;
    @Autowired
    MockMvc mockMvc;



    @Test
    void createFilm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"nisi eiusmod\",\"description\": \"adipisicing\",\"releaseDate\": \"1967-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createFilmWithEmptyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\",\"description\": \"adipisicing\",\"releaseDate\": \"1967-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmWithLongDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"nisi eiusmod\",\"description\": " +
                                "\"adipisicingxbshwbxhbxhhxwbxhwbxswnwjnwjbcwjcbjwckwbdkcjbwjcbjwbcjwbcjwbchb" +
                                "wjchbbcjhdwbcwbcwbcwbchwbcwhbcwbchwjdbcwdbcwbcwkjcbjwkbcwkbckwjbckjwbckjwbckj" +
                                "wbckwjbckwjbckwbckwbckwbckwjbckjwbckwbcgxvwbcgvwhjchwbchwvbchwdvchwbdchwvdchwhdc" +
                                "vwhgdvcjhwdchjwdvchdwcvwhcdvhdjwvhjwcvhwcvhwvchwvcwjhd" +
                                "vcvwc\",\"releaseDate\": \"1967-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmWithIncorrectReleaseDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"nisi eiusmod\",\"description\": \"adipisicing\",\"releaseDate\": \"1800-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void createFilmWithNegativeDuration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\",\"description\": \"adipisicing\",\"releaseDate\": \"1967-03-25\",\"duration\": -100}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmWithEmptyRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getFilms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"nisi eiusmod\",\"description\": \"adipisicing\",\"releaseDate\": \"1967-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/films");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}