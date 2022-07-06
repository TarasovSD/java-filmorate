package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {

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

    @Test
    void updateFilm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"nisi eiusmod\",\"description\": \"adipisicing\",\"releaseDate\": \"1967-03-25\",\"duration\": 100}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"Film Updated\",\n" +
                                "  \"releaseDate\": \"1989-04-17\",\n" +
                                "  \"description\": \"New film update decription\",\n" +
                                "  \"duration\": 190,\n" +
                                "  \"rate\": 4\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}