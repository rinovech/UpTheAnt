package com.uptheant.demo.dto.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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