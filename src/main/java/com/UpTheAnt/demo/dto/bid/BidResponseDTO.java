package com.uptheant.demo.dto.bid;

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
public class BidResponseDTO {
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private Integer userId;
    private Integer auctionId;
}