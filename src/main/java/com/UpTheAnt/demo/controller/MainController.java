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
} 
    
