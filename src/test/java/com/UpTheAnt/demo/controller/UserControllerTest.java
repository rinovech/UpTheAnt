package com.UpTheAnt.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.http.MediaType;

import com.UpTheAnt.demo.model.User;
import com.UpTheAnt.demo.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenUser_whenAdd_thenStatus201andUserReturned() throws Exception {
        User user = new User();
        user.setName("Veronika");
    
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber()) // Проверяем, что id есть и это число
                .andExpect(jsonPath("$.name").value("Veronika")); // Проверяем имя
    }

    @Autowired
    private UserService userService;

    private User createTestUser(String name) {
        User user = new User();
        user.setName(name);
        return userService.createUser(user);
    }

    @Test
    public void givenId_whenGetExistingUser_thenStatus200andUserReturned() throws Exception {
        int id = createTestUser("Veronika").getUserId();
        mockMvc.perform(
                get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id))
                .andExpect(jsonPath("$.name").value("Veronika"));
    }
      
}
