package com.uptheant.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.uptheant.demo.model.User;


@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})

public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setName("Veronika");
        testUser.setUsername("veronika123");
        testUser.setEmail("veronika@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {

        userRepository.delete(testUser);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {

        User savedUser = userRepository.findById(testUser.getUserId())
            .orElse(null);

        assertNotNull(savedUser);
        assertEquals(testUser.getName(), savedUser.getName());
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
    }

    @Test
    void givenUser_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        testUser.setUsername("updatedUsername");
        userRepository.save(testUser);

        User updatedUser = userRepository.findById(testUser.getUserId())
            .orElse(null);

        assertNotNull(updatedUser);
        assertEquals("updatedUsername", updatedUser.getUsername());
    }

}
