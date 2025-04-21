package com.uptheant.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptheant.demo.DemoApplicationTests;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class UserControllerTest extends DemoApplicationTests{

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
        userCreateDTO.setPassword("Password123!");

        mockMvc.perform(
                post("/api/users")
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
        user.setPassword("Password123!");

        User savedUser = userRepository.save(user);

        mockMvc.perform(
                get("/api/users/{id}", savedUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Veronika"))
                .andExpect(jsonPath("$.username").value("veronika123"))
                .andExpect(jsonPath("$.email").value("veronika@example.com"));
    }

    @Test
    public void givenId_whenGetNonExistingUser_thenStatus404() throws Exception {
        mockMvc.perform(
                get("/api/users/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUsers_whenGetAllUsers_thenStatus200andUsersReturned() throws Exception {
        User user1 = new User();
        user1.setName("Veronika");
        user1.setUsername("veronika123");
        user1.setEmail("veronika@example.com");
        user1.setPassword("Password123!");

        User user2 = new User();
        user2.setName("Ivan");
        user2.setUsername("ivan123");
        user2.setEmail("ivan@example.com");
        user2.setPassword("Password123!");

        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Veronika")))
                .andExpect(jsonPath("$[0].username", is("veronika123")))
                .andExpect(jsonPath("$[0].email", is("veronika@example.com")))
                .andExpect(jsonPath("$[1].name", is("Ivan")))
                .andExpect(jsonPath("$[1].username", is("ivan123")))
                .andExpect(jsonPath("$[1].email", is("ivan@example.com")));
    }

    @Test
    public void givenId_whenDeleteExistingUser_thenStatus200andSuccessMessage() throws Exception {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("Password123!");

        User savedUser = userRepository.save(user);

        mockMvc.perform(
                delete("/api/users/{id}", savedUser.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User with ID " + savedUser.getUserId() + " was deleted")));
    }

    @Test
    public void givenId_whenDeleteNonExistingUser_thenStatus404() throws Exception {
        mockMvc.perform(
                delete("/api/users/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidUser_whenAdd_thenStatus400() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        
        mockMvc.perform(
                post("/api/users")
                    .content(objectMapper.writeValueAsString(userCreateDTO))
                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest()); 
    }

    @Test
    public void givenUserByUsername_whenExists_thenStatus200andUserReturned() throws Exception {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("Password123!");

        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/by-username/{username}", "veronika123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("veronika123"));
    }

    @Test
    public void givenUserByUsername_whenNotExists_thenStatus404() throws Exception {
        mockMvc.perform(
                get("/api/users/by-username/{username}", "nonexistent"))
                .andExpect(status().isNotFound());
    }
}