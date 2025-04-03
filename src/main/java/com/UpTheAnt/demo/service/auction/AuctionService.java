package com.uptheant.demo.service.auction;

import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;

import java.util.List;

public interface AuctionService {
    List<AuctionResponseDTO> getAllAuctions();
    AuctionResponseDTO getAuctionById(Integer id);
    AuctionResponseDTO createAuction(AuctionCreateDTO auctionCreateDTO, Integer sellerId);
    void deleteAuction(Integer id);
    void closeAuction(Integer auctionId);
}