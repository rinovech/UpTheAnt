package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uptheant.demo.service.auction.AuctionService;

@Controller
public class MainController {

    @Autowired
    private AuctionService auctionService;

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

    @GetMapping("/allauctions")
    public String activeauctions() {
        return "allauctions"; 
    }

    @GetMapping("/auction/{id}")
    public String getAuctionPage(
        @PathVariable Integer id, 
        Model model) {
        
        model.addAttribute("auction", auctionService.getAuctionById(id));
        return "auction";
    }
} 
    
