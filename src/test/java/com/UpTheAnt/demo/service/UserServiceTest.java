package com.uptheant.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.user.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetAllUsers() {

        User user1 = new User();
        user1.setName("Veronika");

        User user2 = new User();
        user2.setName("Alice");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponseDTO> users = userService.getAllUsers();

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

        UserResponseDTO foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByEmail() {

        User user = new User();
        user.setName("Veronika");
        user.setEmail("veronika@example.com");

        when(userRepository.findUserByEmail("veronika@example.com")).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.getUserByEmail("veronika@example.com");

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findUserByEmail("veronika@example.com");
    }

    @Test
    void testGetUserByUsername() {

        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika");

        when(userRepository.findByUsername("veronika")).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.getUserByUsername("veronika");

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findByUsername("veronika");
    }
    @Test
    void testGetUserByIdNotFound() {

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    void testCreateUser() {

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("Veronika");
        userCreateDTO.setUsername("veronika");
        userCreateDTO.setEmail("veronika@example.com");
        userCreateDTO.setPassword("password123");

        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO createdUser = userService.createUser(userCreateDTO);

        assertNotNull(createdUser);
        assertEquals("Veronika", createdUser.getName());
        assertEquals("veronika", createdUser.getUsername());
        assertEquals("veronika@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        
        User user = new User();
        user.setUserId(1);
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("password123");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserNotFound() {

        when(userRepository.findById(999)).thenReturn(Optional.empty());
    
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999));

        verify(userRepository, never()).delete(any());
    }

}