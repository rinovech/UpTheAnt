package com.uptheant.demo.service.mapper;

import org.springframework.stereotype.Component;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;
import com.uptheant.demo.model.User;

@Component
public class UserMapper {
    public User toEntity(UserCreateDTO dto) {
        return User.builder()
            .name(dto.getName())
            .username(dto.getUsername())
            .email(dto.getEmail())
            .build();
    }

    public UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
            .name(user.getName())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}
