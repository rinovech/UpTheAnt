package com.UpTheAnt.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.http.MediaType;

import com.UpTheAnt.demo.model.User;
import com.UpTheAnt.demo.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUser_whenAdd_thenStatus201andUserReturned() throws Exception {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("password123");
        
    
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber()) 
                .andExpect(jsonPath("$.name").value("Veronika"))
                .andExpect(jsonPath("$.username").value("veronika123"))
                .andExpect(jsonPath("$.email").value("veronika@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));
    }


    private User createTestUser(String name, String username, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Test
    public void givenId_whenGetExistingUser_thenStatus200andUserReturned() throws Exception {
        int id = createTestUser("Veronika", "veronika123", "veronika@example.com", "password123")
                        .getUserId();
        mockMvc.perform(
                get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNumber()) 
                .andExpect(jsonPath("$.name").value("Veronika"))
                .andExpect(jsonPath("$.username").value("veronika123"))
                .andExpect(jsonPath("$.email").value("veronika@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));
    }
      
}
