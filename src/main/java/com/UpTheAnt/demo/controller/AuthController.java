package com.uptheant.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uptheant.demo.model.User;
import com.uptheant.demo.model.UserRole;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.user.UserDetailsImpl;
import com.uptheant.demo.service.auth.JwtUtils;
import com.uptheant.demo.dto.request.LoginRequest;
import com.uptheant.demo.dto.request.SignupRequest;
import com.uptheant.demo.dto.response.JwtResponse;
import com.uptheant.demo.dto.response.MessageResponse;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getAuthInfo() {
        return ResponseEntity.ok(new MessageResponse("Auth endpoints available: /signin, /signup, /signout"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
            
            return ResponseEntity.ok(new JwtResponse(
                jwt, 
                userDetails.getUserId(), 
                userDetails.getName(),
                userDetails.getUsername(), 
                userDetails.getEmail(), 
                role
            ));
        } catch (Exception e) {
            log.error("Authentication error", e);
            return ResponseEntity.badRequest().body(new MessageResponse("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = new User(
                signUpRequest.getName(), 
                signUpRequest.getUsername(), 
                signUpRequest.getEmail(), 
                encoder.encode(signUpRequest.getPassword()), 
                UserRole.USER
            );

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            log.error("Registration error", e);return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(new MessageResponse("Logout successful"));
        } catch (Exception e) {
            log.error("Logout error", e);
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Logout failed: " + e.getMessage()));
        }
    }
}