package com.uptheant.demo.service.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.repository.AuctionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository) {
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