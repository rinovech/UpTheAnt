package com.uptheant.demo.service;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;
import com.uptheant.demo.dto.user.UserUpdateDTO;

import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.UserMapper;
import com.uptheant.demo.service.user.UserServiceImpl;
import com.uptheant.demo.service.validation.UserValidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetAllUsers() {
        
        User user1 = createTestUser(1, "Veronika", "veronika", "veronika@example.com");
        User user2 = createTestUser(2, "Alice", "alice", "alice@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponseDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("Veronika", users.get(0).getName());
        assertEquals("Alice", users.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        
        User user = createTestUser(1, "Veronika", "veronika", "veronika@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(999);
        });

        assertEquals("User not found with ID: 999", exception.getMessage());
        verify(userRepository, times(1)).findById(999);
    }

    @Test
    void testGetUserByEmail() {
        
        User user = createTestUser(1, "Veronika", "veronika", "veronika@example.com");
        when(userRepository.findUserByEmail("veronika@example.com")).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.getUserByEmail("veronika@example.com");

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findUserByEmail("veronika@example.com");
    }

    @Test
    void testGetUserByUsername() {
        
        User user = createTestUser(1, "Veronika", "veronika", "veronika@example.com");
        when(userRepository.findByUsername("veronika")).thenReturn(Optional.of(user));

        UserResponseDTO foundUser = userService.getUserByUsername("veronika");

        assertNotNull(foundUser);
        assertEquals("Veronika", foundUser.getName());
        verify(userRepository, times(1)).findByUsername("veronika");
    }

    @Test
    void testCreateUser() {

        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName("Veronika");
        createDTO.setUsername("veronika");
        createDTO.setEmail("veronika@example.com");
        createDTO.setPassword("password123");

        User savedUser = new User();
        savedUser.setUserId(1);
        savedUser.setName("Veronika");
        savedUser.setUsername("veronika");
        savedUser.setEmail("veronika@example.com");
        savedUser.setPassword("password123");
    
        UserResponseDTO expectedDto = new UserResponseDTO();
        expectedDto.setName("Veronika");
        expectedDto.setUsername("veronika");
        expectedDto.setEmail("veronika@example.com");
  

        doNothing().when(userValidator).validateCreation(createDTO);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

        UserResponseDTO result = userService.createUser(createDTO);

        assertNotNull(result);
        assertEquals("Veronika", result.getName());
        assertEquals("veronika", result.getUsername());
        assertEquals("veronika@example.com", result.getEmail());

        verify(userValidator).validateCreation(createDTO);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void testDeleteUser() {
        
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        
        when(userRepository.existsById(999)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(999);
        });

        assertEquals("User not found with ID: 999", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateUser() {

        Integer userId = 1;
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setPassword("newpassword");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setName("Old Name");
        existingUser.setUsername("user");
        existingUser.setEmail("user@example.com");
        existingUser.setPassword("oldpassword");
    
        User updatedUser = new User();
        updatedUser.setUserId(userId);
        updatedUser.setName("New Name"); 
        updatedUser.setUsername("user"); 
        updatedUser.setEmail("user@example.com"); 
        updatedUser.setPassword("newpassword"); 
    
        UserResponseDTO expectedDto = new UserResponseDTO();
        expectedDto.setName("New Name");
        expectedDto.setUsername("user");
        expectedDto.setEmail("user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);

        UserResponseDTO result = userService.updateUser(userId, updateDTO);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("user", result.getUsername());
        assertEquals("user@example.com", result.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(updatedUser);
    }

    private User createTestUser(Integer id, String name, String username, String email) {
        User user = new User();
        user.setUserId(id);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password");
        return user;
    }
}