package com.uptheant.demo.dto.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uptheant.demo.dto.user.UserBidDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionParticipationDTO {
    private Integer auctionId;
    private String name;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal currentBid;
    private BigDecimal minBidStep;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UserBidDTO> userBids;
}
