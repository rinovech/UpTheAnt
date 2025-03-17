package com.uptheant.demo.service.bid;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;

import java.util.List;

public interface BidService {
    List<BidResponseDTO> getAllBids();
    BidResponseDTO getBidById(Integer id);
    BidResponseDTO createBid(BidCreateDTO bidCreateDTO, Integer userId, Integer auctionId);
    void deleteBid(Integer id);
} 