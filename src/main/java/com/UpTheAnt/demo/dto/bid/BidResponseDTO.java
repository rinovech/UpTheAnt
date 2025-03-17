package com.uptheant.demo.dto.bid;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BidResponseDTO {
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private Integer userId;
    private Integer auctionId;
}