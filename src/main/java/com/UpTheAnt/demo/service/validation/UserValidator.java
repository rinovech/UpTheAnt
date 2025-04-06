package com.uptheant.demo.service.validation;

import org.springframework.stereotype.Service;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;


    public void validateCreation(UserCreateDTO dto) {

        if (dto == null) {
            throw new BusinessRuleException("User data cannot be null");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("Email already registered");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessRuleException("Username already taken");
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessRuleException("Invalid email format");
        }

        validatePassword(dto.getPassword());

        if (dto.getUsername().length() < 4 || dto.getUsername().length() > 20) {
            throw new BusinessRuleException("Username must be 4-20 characters");
        }
        if (!dto.getUsername().matches("^[a-zA-Z0-9._-]+$")) {
            throw new BusinessRuleException("Username contains invalid characters");
        }

        if (dto.getName().trim().isEmpty()) {
            throw new BusinessRuleException("Name cannot be empty");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new BusinessRuleException("Password must be at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessRuleException("Password must contain uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new BusinessRuleException("Password must contain lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new BusinessRuleException("Password must contain digit 0-9");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new BusinessRuleException("Password must contain special character: !@#$%^&*()");
        }
    }
}
