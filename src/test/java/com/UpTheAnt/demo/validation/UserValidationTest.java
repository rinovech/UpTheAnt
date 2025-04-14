package com.uptheant.demo.validation;

import com.uptheant.demo.DemoApplicationTests;
import com.uptheant.demo.controller.UserController;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@SpringBootTest
@Transactional
public class UserValidationTest extends DemoApplicationTests {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private UserCreateDTO createValidUserDTO() {
        UserCreateDTO validDTO = new UserCreateDTO();
        validDTO.setName("Veronika");
        validDTO.setUsername("testuser_" + UUID.randomUUID().toString().substring(0, 8));
        validDTO.setEmail("testuser_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        validDTO.setPassword("Password123!");
        return validDTO;
    }

    @Test
    void createUser_withValidData_shouldSucceed() {
        UserCreateDTO validDTO = createValidUserDTO();

        ResponseEntity<?> response = userController.createUser(validDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createUser_withExistingEmail_shouldFail() {
        UserCreateDTO firstDTO = createValidUserDTO();
        userController.createUser(firstDTO);

        UserCreateDTO duplicateEmailDTO = createValidUserDTO();
        duplicateEmailDTO.setEmail(firstDTO.getEmail());
        duplicateEmailDTO.setUsername(duplicateEmailDTO.getUsername() + "_different");

        ResponseEntity<?> response = userController.createUser(duplicateEmailDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withInvalidEmailFormat_shouldFail() {
        UserCreateDTO invalidEmailDTO = createValidUserDTO();
        invalidEmailDTO.setEmail("invalid");

        ResponseEntity<?> response = userController.createUser(invalidEmailDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withShortUsername_shouldFail() {
        UserCreateDTO shortUsernameDTO = createValidUserDTO();
        shortUsernameDTO.setUsername("ver");

        ResponseEntity<?> response = userController.createUser(shortUsernameDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withInvalidUsernameCharacters_shouldFail() {
        UserCreateDTO invalidUsernameDTO = createValidUserDTO();
        invalidUsernameDTO.setUsername("ver#o");

        ResponseEntity<?> response = userController.createUser(invalidUsernameDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withShortPassword_shouldFail() {
        UserCreateDTO shortPasswordDTO = createValidUserDTO();
        shortPasswordDTO.setPassword("pass");

        ResponseEntity<?> response = userController.createUser(shortPasswordDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withoutUppercasePassword_shouldFail() {
        UserCreateDTO passwordDTO = createValidUserDTO();
        passwordDTO.setPassword("password123!");

        ResponseEntity<?> response = userController.createUser(passwordDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withoutLowercasePassword_shouldFail() {
        UserCreateDTO passwordDTO = createValidUserDTO();
        passwordDTO.setPassword("PASSWORD123!");

        ResponseEntity<?> response = userController.createUser(passwordDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withoutDigitPassword_shouldFail() {
        UserCreateDTO passwordDTO = createValidUserDTO();
        passwordDTO.setPassword("Password!");

        ResponseEntity<?> response = userController.createUser(passwordDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withoutSpecialCharacterPassword_shouldFail() {
        UserCreateDTO passwordDTO = createValidUserDTO();
        passwordDTO.setPassword("Password123");

        ResponseEntity<?> response = userController.createUser(passwordDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createUser_withEmptyName_shouldFail() {
        UserCreateDTO emptyNameDTO = createValidUserDTO();
        emptyNameDTO.setName("");

        ResponseEntity<?> response = userController.createUser(emptyNameDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}