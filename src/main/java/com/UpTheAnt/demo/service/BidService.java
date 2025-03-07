package com.uptheant.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptheant.demo.model.Bid;
import com.uptheant.demo.repository.BidRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public Optional<Bid> getBidById(Integer id) {
        return bidRepository.findById(id);
    }

    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public void deleteBid(Integer id) {
        bidRepository.deleteById(id);
    }
}