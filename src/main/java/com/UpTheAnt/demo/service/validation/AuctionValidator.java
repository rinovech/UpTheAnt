package com.uptheant.demo.service.validation;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.exception.BusinessRuleException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionValidator {
    private final Clock clock;

    public void validateCreation(AuctionCreateDTO dto) {
        if (dto.getStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new BusinessRuleException("Auction cannot start in the past");
        }
        
        if (dto.getEndTime().isBefore(dto.getStartTime().plusMinutes(5))) {
            throw new BusinessRuleException("Auction duration must be at least 5 minutes");
        }
        
        if (dto.getMinBidStep().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Bid step must be positive");
        }
    }

    public void validateBidPlacement(Auction auction, BidCreateDTO bid) {
        if (auction.isStatus()) {
            throw new BusinessRuleException("Auction is already closed");
        }
        
        if (auction.getEndTime().isBefore(LocalDateTime.now(clock))) {
            throw new BusinessRuleException("Auction has already ended");
        }
        
        BigDecimal minBid = auction.getCurrentBid() == null 
            ? auction.getStartPrice()
            : auction.getCurrentBid().add(auction.getMinBidStep());
            
        if (bid.getBidAmount().compareTo(minBid) < 0) {
            throw new BusinessRuleException("Bid amount is too low");
        }
    }

    public void validateClosing(Auction auction) {
        if (auction.isStatus()) {
            throw new BusinessRuleException("Auction is already closed");
        }
        
        if (auction.getEndTime().isAfter(LocalDateTime.now(clock))) {
            throw new BusinessRuleException("Cannot close auction before end time");
        }
    }

}
