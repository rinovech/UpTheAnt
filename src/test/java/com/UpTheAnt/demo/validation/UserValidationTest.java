package com.uptheant.demo.validation;

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

@SpringBootTest
@Transactional
public class UserValidationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_withValidData_shouldSucceed() {
        UserCreateDTO validDTO = new UserCreateDTO();
        validDTO.setName("Veronika");
        validDTO.setUsername("veronika");
        validDTO.setEmail("veronika@example.com");
        validDTO.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(validDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createUser_withExistingEmail_shouldFail() {

        UserCreateDTO validDTO1 = new UserCreateDTO();
        validDTO1.setName("Veronika");
        validDTO1.setUsername("veronika");
        validDTO1.setEmail("veronika@example.com");
        validDTO1.setPassword("Password123!");
        
        userController.createUser(validDTO1);

        UserCreateDTO validDTO2 = new UserCreateDTO();
        validDTO2.setName("Veronika");
        validDTO2.setUsername("veronika1");
        validDTO2.setEmail("veronika@example.com");
        validDTO2.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(validDTO2);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already registered", response.getBody());
    }

    @Test
    void createUser_withInvalidEmailFormat_shouldFail() {

        UserCreateDTO invalidEmailUser = new UserCreateDTO();
        invalidEmailUser.setName("Veronika");
        invalidEmailUser.setUsername("veronika");
        invalidEmailUser.setEmail("invalid");
        invalidEmailUser.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(invalidEmailUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Invalid email format", response.getBody());
    }

    @Test
    void createUser_withShortUsername_shouldFail() {

        UserCreateDTO shortUsernameUser = new UserCreateDTO();
        shortUsernameUser.setName("Veronika");
        shortUsernameUser.setUsername("ver");
        shortUsernameUser.setEmail("veronika@mail.ru");
        shortUsernameUser.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(shortUsernameUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username must be 4-20 characters", response.getBody());
    }

    @Test
    void createUser_withInvalidUsernameCharacters_shouldFail() {
        UserCreateDTO invalidUsernameUser = new UserCreateDTO();
        invalidUsernameUser.setName("Veronika");
        invalidUsernameUser.setUsername("ver#o");
        invalidUsernameUser.setEmail("veronika@mail.ru");
        invalidUsernameUser.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(invalidUsernameUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Username contains invalid characters", response.getBody());
    }

    @Test
    void createUser_withshortPassword_shouldFail() {
        
        UserCreateDTO weakPasswordUser = new UserCreateDTO();
        weakPasswordUser.setName("Veronika");
        weakPasswordUser.setUsername("veronika");
        weakPasswordUser.setEmail("veronika@mail.ru");
        weakPasswordUser.setPassword("pass");

        ResponseEntity<?> response = userController.createUser(weakPasswordUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Password must be at least 8 characters", response.getBody());
    }

    @Test
    void createUser_witupperPassword_shouldFail() {
        
        UserCreateDTO weakPasswordUser = new UserCreateDTO();
        weakPasswordUser.setName("Veronika");
        weakPasswordUser.setUsername("veronika");
        weakPasswordUser.setEmail("veronika@mail.ru");
        weakPasswordUser.setPassword("password");

        ResponseEntity<?> response = userController.createUser(weakPasswordUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Password must contain uppercase letter", response.getBody());
    }

    @Test
    void createUser_withlowPassword_shouldFail() {
        
        UserCreateDTO weakPasswordUser = new UserCreateDTO();
        weakPasswordUser.setName("Veronika");
        weakPasswordUser.setUsername("veronika");
        weakPasswordUser.setEmail("veronika@mail.ru");
        weakPasswordUser.setPassword("PASSWORD");

        ResponseEntity<?> response = userController.createUser(weakPasswordUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Password must contain lowercase letter", response.getBody());
    }

    @Test
    void createUser_withdigitPassword_shouldFail() {
        
        UserCreateDTO weakPasswordUser = new UserCreateDTO();
        weakPasswordUser.setName("Veronika");
        weakPasswordUser.setUsername("veronika");
        weakPasswordUser.setEmail("veronika@mail.ru");
        weakPasswordUser.setPassword("Password");

        ResponseEntity<?> response = userController.createUser(weakPasswordUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Password must contain digit 0-9", response.getBody());
    }

    @Test
    void createUser_withWeakPassword_shouldFail() {
        
        UserCreateDTO weakPasswordUser = new UserCreateDTO();
        weakPasswordUser.setName("Veronika");
        weakPasswordUser.setUsername("veronika");
        weakPasswordUser.setEmail("veronika@mail.ru");
        weakPasswordUser.setPassword("Password12");

        ResponseEntity<?> response = userController.createUser(weakPasswordUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Password must contain special character: !@#$%^&*()", response.getBody());
    }

    @Test
    void createUser_withEmptyName_shouldFail() {

        UserCreateDTO emptyNameUser = new UserCreateDTO();
        emptyNameUser.setName("");
        emptyNameUser.setUsername("veronika");
        emptyNameUser.setEmail("veronika@mail.ru");
        emptyNameUser.setPassword("Password123!");

        ResponseEntity<?> response = userController.createUser(emptyNameUser);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Name cannot be empty", response.getBody());
    }
}