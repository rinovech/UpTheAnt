package com.uptheant.demo.service.validation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BidValidator {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public void validateBidCreation(BidCreateDTO dto, Integer userId, Integer auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found"));

        if (!userRepository.existsById(userId)) {
            throw new BusinessRuleException("User not found");
        }

        if (!auction.isStatus()) {
            throw new BusinessRuleException("Auction is not active");
        }

        LocalDateTime now = LocalDateTime.now();
        if (auction.getStartTime().isAfter(now)) {
            throw new BusinessRuleException("Auction has not started yet");
        }

        if (auction.getEndTime().isBefore(now)) {
            throw new BusinessRuleException("Auction has already ended");
        }

        BigDecimal currentBid = auction.getCurrentBid() != null 
            ? auction.getCurrentBid() 
            : auction.getStartPrice();

        BigDecimal minBidAmount = currentBid.add(auction.getMinBidStep());

        if (dto.getBidAmount().compareTo(minBidAmount) < 0) {
            throw new BusinessRuleException(String.format(
                "Bid amount must be at least %s (current bid + min bid step)", 
                minBidAmount
            ));
        }
    }
}

