package com.uptheant.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uptheant.demo.dto.request.LoginRequest;
import com.uptheant.demo.dto.request.SignupRequest;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/view/auth")
@RequiredArgsConstructor
public class AuthViewController {
    private final AuthController authController;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegisterForm() {
        return "signup";
    }
    
    @PostMapping("/signin")
    public String processLogin(
        @RequestParam String username,
        @RequestParam String password,
        RedirectAttributes redirectAttributes) {
        
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            ResponseEntity<?> response = authController.authenticateUser(loginRequest);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/lk";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
        }
        return "redirect:/view/auth/login?error";
    }
    
    @PostMapping("/signup")
    public String processRegistration(
        @RequestParam String name,
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        RedirectAttributes redirectAttributes) {
        
        try {
            SignupRequest signupRequest = new SignupRequest(name, username, email, password);
            ResponseEntity<?> response = authController.registerUser(signupRequest);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/view/auth/login?success";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed");
        }
        return "redirect:/view/auth/register?error";
    }
}
