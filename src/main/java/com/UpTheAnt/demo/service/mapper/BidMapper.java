package com.uptheant.demo.service.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.model.Bid;

@Component
public class BidMapper {

    public Bid toEntity(BidCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Bid.builder()
                .bidAmount(dto.getBidAmount())
                .bidTime(LocalDateTime.now())
                .build();
    }

    public BidResponseDTO toDto(Bid bid) {
        return BidResponseDTO.builder()
                .bidAmount(bid.getBidAmount())
                .bidTime(bid.getBidTime())
                .userId(bid.getUser().getUserId())
                .auctionId(bid.getAuction().getAuctionId())
                .build();
    }
}