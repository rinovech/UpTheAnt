package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Auction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    private final List<Auction> auctions = new ArrayList<>();
    private Integer nextAuctionId = 1;

    public List<Auction> getAllAuctions() {
        return auctions;
    }

    public Optional<Auction> getAuctionById(Integer id) {
        return auctions.stream()
                .filter(auction -> auction.getAuctionId().equals(id))
                .findFirst();
    }

    public Auction createAuction(Auction auction) {
        auction.setAuctionId(nextAuctionId++);
        auctions.add(auction);
        return auction;
    }

    public void deleteAuction(Integer id) {
        auctions.removeIf(auction -> auction.getAuctionId().equals(id));
    }
}