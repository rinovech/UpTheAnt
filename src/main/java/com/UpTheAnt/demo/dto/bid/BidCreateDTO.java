package com.uptheant.demo.dto.bid;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidCreateDTO {
    private BigDecimal bidAmount;
}