package com.uptheant.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @GetMapping("/lk")
    public String lk() {
        return "lk"; 
    }

    @GetMapping("/lk/userbids")
    public String lkuserbids() {
        return "userbids"; 
    }

    @GetMapping("/lk/userauctions")
    public String lkuserauctions() {
        return "userauctions"; 
    }
} 
    
