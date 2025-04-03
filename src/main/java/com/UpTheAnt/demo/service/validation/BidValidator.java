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
            throw new EntityNotFoundException("User not found");
        }

        if (auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Auction has already ended");
        }

        BigDecimal minBid = auction.getCurrentBid() != null ? 
                auction.getCurrentBid().add(auction.getMinBidStep()) :
                auction.getStartPrice();
        
        if (dto.getBidAmount().compareTo(minBid) < 0) {
            throw new BusinessRuleException("Bid amount must be at least " + minBid);
        }
    }
}