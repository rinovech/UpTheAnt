package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Bid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private final List<Bid> bids = new ArrayList<>();
    private Integer nextBidId = 1;

    public List<Bid> getAllBids() {
        return bids;
    }

    public Optional<Bid> getBidById(Integer id) {
        return bids.stream()
                .filter(bid -> bid.getBidId().equals(id))
                .findFirst();
    }

    public Bid createBid(Bid bid) {
        bid.setBidId(nextBidId++);
        bids.add(bid);
        return bid;
    }

    public void deleteBid(Integer id) {
        bids.removeIf(bid -> bid.getBidId().equals(id));
    }
}