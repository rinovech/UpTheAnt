package com.uptheant.demo.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uptheant.demo.service.validation.AuctionValidator;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
    
    @Bean
    public AuctionValidator auctionValidator(Clock clock) {
        return new AuctionValidator(clock);
    }
}