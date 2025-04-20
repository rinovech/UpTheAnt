package com.uptheant.demo.dto.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBidDTO {
    private String bidderUsername;  
    private BigDecimal amount;   
    private LocalDateTime bidTime; 
}