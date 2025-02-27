package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Auction;
import com.UpTheAnt.demo.repository.AuctionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuctionById(Integer id) {
        return auctionRepository.findById(id);
    }

    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public void deleteAuction(Integer id) {
        auctionRepository.deleteById(id);
    }
}