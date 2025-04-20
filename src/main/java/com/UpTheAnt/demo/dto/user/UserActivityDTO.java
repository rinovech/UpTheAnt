package com.uptheant.demo.dto.user;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserActivityDTO {
    public enum ActivityType {
        BID, 
        AUCTION_CREATED, 
        AUCTION_FINISHED, 
        AUCTION_WON
    }

    private ActivityType type;
    private String auctionName;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public static UserActivityDTOBuilder builder() {
        return new UserActivityDTOBuilder();
    }
}
