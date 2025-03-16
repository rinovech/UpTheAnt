package com.uptheant.demo.service.bid;

import com.uptheant.demo.model.Bid;

import java.util.List;
import java.util.Optional;

public interface BidService {
    List<Bid> getAllBids();
    Optional<Bid> getBidById(Integer id);
    Bid createBid(Bid bid);
    void deleteBid(Integer id);
} 