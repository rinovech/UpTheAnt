package com.uptheant.demo.service.mapper;

import org.springframework.stereotype.Component;

import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.uptheant.demo.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class AuctionMapper {
    private final UserRepository userRepository;

    public Auction toEntity(AuctionCreateDTO dto, Integer sellerId) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        
        return Auction.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .startPrice(dto.getStartPrice())
            .minBidStep(dto.getMinBidStep())
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .status(false)
            .user(seller)
            .build();
    }

    public AuctionResponseDTO toDto(Auction auction) {
        return AuctionResponseDTO.builder()
            .auctionId(auction.getAuctionId())
            .name(auction.getName())
            .description(auction.getDescription())
            .startPrice(auction.getStartPrice())
            .currentBid(auction.getCurrentBid())
            .minBidStep(auction.getMinBidStep())
            .startTime(auction.getStartTime())
            .endTime(auction.getEndTime())
            .status(auction.isStatus())
            .sellerId(auction.getUser().getUserId())
            .build();
    }
}