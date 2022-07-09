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

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createUserWithIncorrectLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"      \": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createUserWithEmptyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"dolore\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));
    }

    @Test
    void createUserWithFutureBirthday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"2500-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"login\": \"new\",\n" +
                                "  \"name\": \"New Name\",\n" +
                                "  \"email\": \"mail1@mail.ru\",\n" +
                                "  \"birthday\": \"1956-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"login\": \"new\",\n" +
                        "  \"name\": \"New Name\",\n" +
                        "  \"email\": \"mail1@mail.ru\",\n" +
                        "  \"birthday\": \"1956-08-20\"\n" +
                        "}"));
    }

    @Test
    public void getUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/users");
        ResultActions result = mockMvc.perform(request);
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}