package com.uptheant.demo.service.user;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;
import com.uptheant.demo.dto.user.UserUpdateDTO;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper; 

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setName(userCreateDTO.getName().trim());
        user.setUsername(userCreateDTO.getUsername().trim());
        user.setEmail(userCreateDTO.getEmail().trim().toLowerCase());
        user.setPassword(userCreateDTO.getPassword());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        return userRepository.findUserByEmail(email.trim().toLowerCase())
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username.trim())
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    private UserResponseDTO convertToDto(User user) {
        return UserResponseDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}