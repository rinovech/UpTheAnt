package com.uptheant.demo.service.validation;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.model.Auction;

@Service
public class AuctionValidator {
    private final Clock clock;

    public AuctionValidator(Clock clock) {
        this.clock = clock;
    }

    public void validateCreation(AuctionCreateDTO dto) {

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new BusinessRuleException("Start and end times must be specified");
        }

        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BusinessRuleException("Start time must be before end time");
        }

        if (dto.getStartPrice() == null || dto.getStartPrice().doubleValue() <= 0) {
            throw new BusinessRuleException("Start price must be positive");
        }

        if (dto.getMinBidStep() == null || dto.getMinBidStep().doubleValue() <= 0) {
            throw new BusinessRuleException("Minimum bid step must be positive");
        }
    }

    public void validateClosing(Auction auction) {

        LocalDateTime now = LocalDateTime.now(clock);

        if (!auction.isStatus()) {
            throw new BusinessRuleException("Auction is already closed");
        }

        if (auction.getEndTime().isAfter(now)) {
            throw new BusinessRuleException("Auction end time has not yet arrived");
        }
    }
}

