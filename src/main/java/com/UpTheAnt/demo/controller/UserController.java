package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.service.user.UserService;
import com.uptheant.demo.exception.BusinessRuleException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.uptheant.demo.dto.auction.AuctionParticipationDTO;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "Операции с пользователями")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
        @Parameter(description = "ID пользователя", example = "1") @PathVariable Integer id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Пользователь создан"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "409", description = "Конфликт данных")
    })
    @PostMapping
    public ResponseEntity<?> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания пользователя",
            required = true,
            content = @Content(
                schema = @Schema(implementation = UserCreateDTO.class),
                examples = @ExampleObject(
                        name = "Пример запроса",
                        value = """
                        {
                            "name": "Veronika",
                            "username": "veronika",
                            "email": "veronika@example.com",
                            "password": "Password1!"
                        }
                        """
                    )
            )
        )
        @Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            UserResponseDTO createdUser = userService.createUser(userCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (BusinessRuleException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь удален"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
        @Parameter(description = "ID пользователя", example = "1") @PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User with ID " + id + " was deleted");
        } catch (BusinessRuleException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Найти пользователя по имени")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> getUserByUsername(
        @Parameter(description = "Username пользователя", example = "veronika") 
        @PathVariable String username) {
        try {
            UserResponseDTO user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (BusinessRuleException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {

        UserResponseDTO user = userService.getUserByUsername(userDetails.getUsername());
        
        return ResponseEntity.ok(
            new UserResponseDTO(
                user.getName(),
                user.getUsername(),
                user.getEmail()
            )
        );
    }

    @GetMapping("/{username}/auction-participations")
    public ResponseEntity<List<AuctionParticipationDTO>> getUserAuctionParticipations(
            @PathVariable String username) {
        List<AuctionParticipationDTO> participations = 
                userService.getUserAuctionParticipations(username);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/{username}/created-auctions")
    public ResponseEntity<List<AuctionParticipationDTO>> getUserAuctionCreations(
            @PathVariable String username) {
        List<AuctionParticipationDTO> participations = 
                userService.getUserAuctionCreations(username);
        return ResponseEntity.ok(participations);
    }
}
