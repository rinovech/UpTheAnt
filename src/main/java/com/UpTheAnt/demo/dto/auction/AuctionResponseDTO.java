package com.uptheant.demo.dto.auction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionResponseDTO {
    private String name;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentBid;
    private BigDecimal minBidStep;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean status;
    private Integer sellerId;
}