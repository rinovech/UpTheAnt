package com.uptheant.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll(); 
}

    @Test
    public void givenUser_whenAdd_thenStatus201andUserReturned() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("Veronika");
        userCreateDTO.setUsername("veronika123");
        userCreateDTO.setEmail("veronika@example.com");
        userCreateDTO.setPassword("password123");

        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(userCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Veronika"))
                .andExpect(jsonPath("$.username").value("veronika123"))
                .andExpect(jsonPath("$.email").value("veronika@example.com"));
    }

    @Test
    public void givenId_whenGetExistingUser_thenStatus200andUserReturned() throws Exception {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("password123");

        User savedUser = userRepository.save(user);

        mockMvc.perform(
                get("/users/{id}", savedUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Veronika"))
                .andExpect(jsonPath("$.username").value("veronika123"))
                .andExpect(jsonPath("$.email").value("veronika@example.com"));
    }

    @Test
    public void givenId_whenGetNonExistingUser_thenStatus404() throws Exception {
        mockMvc.perform(
                get("/users/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUsers_whenGetAllUsers_thenStatus200andUsersReturned() throws Exception {
        User user1 = new User();
        user1.setName("Veronika");
        user1.setUsername("veronika123");
        user1.setEmail("veronika@example.com");
        user1.setPassword("password123");

        User user2 = new User();
        user2.setName("Ivan");
        user2.setUsername("ivan123");
        user2.setEmail("ivan@example.com");
        user2.setPassword("password123");

        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Veronika"))
                .andExpect(jsonPath("$[0].username").value("veronika123"))
                .andExpect(jsonPath("$[0].email").value("veronika@example.com"))
                .andExpect(jsonPath("$[1].name").value("Ivan"))
                .andExpect(jsonPath("$[1].username").value("ivan123"))
                .andExpect(jsonPath("$[1].email").value("ivan@example.com"));
    }

    @Test
    public void givenId_whenDeleteExistingUser_thenStatus200andSuccessMessage() throws Exception {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("password123");

        User savedUser = userRepository.save(user);

        mockMvc.perform(
                delete("/users/{id}", savedUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID " + savedUser.getUserId() + " was successfully deleted."));
    }

    @Test
    public void givenId_whenDeleteNonExistingUser_thenStatus404() throws Exception {
        mockMvc.perform(
                delete("/users/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidUser_whenAdd_thenStatus400() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName(null); 
        userCreateDTO.setUsername(null); 
        userCreateDTO.setEmail(null); 
        userCreateDTO.setPassword(null); 
    
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(userCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest()); 
    }
}