package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.User;
import com.UpTheAnt.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {

        User user1 = new User();
        user1.setName("Veronika");

        User user2 = new User();
        user2.setName("Alice");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("Veronika", users.get(0).getName());
        assertEquals("Alice", users.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {

        User user = new User();
        user.setName("Veronika");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1);

        assertTrue(foundUser.isPresent());
        assertEquals("Veronika", foundUser.get().getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(999);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    void testCreateUser() {

        User user = new User();
        user.setName("Veronika");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Veronika", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {

        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {

        doNothing().when(userRepository).deleteById(999);

        userService.deleteUser(999);

        verify(userRepository, times(1)).deleteById(999);
    }

    // @Test
    // void testCreateUserWithInvalidData() {

    //     User user = new User();
    //     user.setName(""); // Пустое имя
    //     user.setUsername(""); // Пустой username
    //     user.setEmail(""); // Пустой email
    //     user.setPassword(""); // Пустой пароль

    //     when(userRepository.save(user)).thenReturn(user);

    //     User createdUser = userService.createUser(user);

    //     assertNotNull(createdUser);
    //     assertEquals("", createdUser.getName());
    //     assertEquals("", createdUser.getUsername());
    //     assertEquals("", createdUser.getEmail());
    //     assertEquals("", createdUser.getPassword());
    //     verify(userRepository, times(1)).save(user);
    // }

    @Test
    void testGetUserByIdWithNegativeId() {

        when(userRepository.findById(-1)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(-1);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(-1);
    }

    @Test
    void testDeleteUserWithNegativeId() {

        doNothing().when(userRepository).deleteById(-1);

        userService.deleteUser(-1);

        verify(userRepository, times(1)).deleteById(-1);
    }
}