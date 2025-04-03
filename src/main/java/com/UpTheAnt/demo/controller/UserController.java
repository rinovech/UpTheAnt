package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Операции с пользователями")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Получить всех пользователей",
        description = "Возвращает список всех зарегистрированных пользователей"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Успешное получение списка пользователей",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserResponseDTO.class),
            examples = @ExampleObject(value = "[{\"name\":\"Veronika\",\"username\":\"veronika\",\"email\":\"veronika@example.com\"}]")
        )
    )
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @Operation(
        summary = "Получить пользователя по ID",
        description = "Возвращает подробную информацию о пользователе по его идентификатору"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(value = "[{\"name\":\"Veronika\",\"username\":\"veronika\",\"email\":\"veronika@example.com\"}]")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(examples = @ExampleObject(value = "{}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
        @Parameter(description = "ID пользователя", example = "1")
        @PathVariable Integer id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
        summary = "Создать нового пользователя",
        description = "Регистрирует нового пользователя в системе"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно создан",
            content = @Content(
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(value = "[{\"name\":\"Veronika\",\"username\":\"veronika\",\"email\":\"veronika@example.com\"}]")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Невалидные данные пользователя",
            content = @Content(examples = @ExampleObject(value = "{\"error\":\"Invalid email format\"}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания пользователя",
            required = true,
            content = @Content(
                schema = @Schema(implementation = UserCreateDTO.class),
                examples = @ExampleObject(value = "[{\"name\":\"Veronika\",\"username\":\"veronika\",\"email\":\"veronika@example.com\",\"password\":\"password\"}")
            )
        )
        @Valid @RequestBody UserCreateDTO userCreateDTO) {
        return userService.createUser(userCreateDTO);
    }

    @Operation(
        summary = "Удалить пользователя",
        description = "Удаляет пользователя по его идентификатору"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно удален",
            content = @Content(examples = @ExampleObject(value = "\"User with ID 1 was successfully deleted.\""))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(examples = @ExampleObject(value = "\"User not found with ID: 1\"")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
        @Parameter(description = "ID пользователя для удаления", example = "1")
        @PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User with ID " + id + " was successfully deleted.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(
        summary = "Найти пользователя по имени",
        description = "Возвращает пользователя по его username"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(
                schema = @Schema(implementation = UserResponseDTO.class),
                examples = @ExampleObject(value = "[{\"name\":\"Veronika\",\"username\":\"veronika\",\"email\":\"veronika@example.com\"}]")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(examples = @ExampleObject(value = "{}")))
    })
    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(
        @Parameter(description = "Username пользователя", example = "veronika")
        @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}