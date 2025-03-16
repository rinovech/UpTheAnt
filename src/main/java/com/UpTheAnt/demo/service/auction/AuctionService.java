package com.uptheant.demo.service.auction;

import com.uptheant.demo.model.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionService {
    List<Auction> getAllAuctions();
    Optional<Auction> getAuctionById(Integer id);
    Auction createAuction(Auction auction);
    void deleteAuction(Integer id);
}