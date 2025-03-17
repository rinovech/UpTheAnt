package com.uptheant.demo.dto.auction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AuctionCreateDTO {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal startPrice;

    @NotNull
    private BigDecimal minBidStep;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}