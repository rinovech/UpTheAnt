package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void testGetAllUsers() {
        UserService userService = new UserService();
        User user1 = new User();
        user1.setName("Veronika");
        userService.createUser(user1);

        User user2 = new User();
        user2.setName("Veronika");
        userService.createUser(user2);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("Veronika", users.get(0).getName());
        assertEquals("Veronika", users.get(1).getName());
    }

    @Test
    void testGetUserById() {
        UserService userService = new UserService();
        User user = new User();
        user.setName("Veronika");
        userService.createUser(user);

        Optional<User> foundUser = userService.getUserById(1);

        assertTrue(foundUser.isPresent());
        assertEquals("Veronika", foundUser.get().getName());
    }

    @Test
    void testGetUserByIdNotFound() {
        UserService userService = new UserService();
        Optional<User> foundUser = userService.getUserById(999);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testCreateUser() {
        UserService userService = new UserService();
        User user = new User();
        user.setName("Veronika");

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getUserId());
        assertEquals("Veronika", createdUser.getName());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void testDeleteUser() {
        UserService userService = new UserService();
        User user = new User();
        user.setName("Veronika");
        userService.createUser(user);

        userService.deleteUser(1);

        assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    void testDeleteUserNotFound() {
        UserService userService = new UserService();
        userService.deleteUser(999); 

        assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    void testCreateUserWithInvalidData() {
        UserService userService = new UserService();
        User user = new User();
        user.setName(""); // Пустое имя
        user.setUsername(""); // Пустой username
        user.setEmail(""); // Пустой email
        user.setPassword(""); // Пустой пароль

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getUserId());
        assertEquals("", createdUser.getName());
        assertEquals("", createdUser.getUsername());
        assertEquals("", createdUser.getEmail());
        assertEquals("", createdUser.getPassword());
    }

    @Test
    void testGetUserByIdWithNegativeId() {
        UserService userService = new UserService();
        Optional<User> foundUser = userService.getUserById(-1);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testDeleteUserWithNegativeId() {
        UserService userService = new UserService();
        userService.deleteUser(-1); 

        assertEquals(0, userService.getAllUsers().size());
    }
}