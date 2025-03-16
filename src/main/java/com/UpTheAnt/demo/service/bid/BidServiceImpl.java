package com.uptheant.demo.service.bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptheant.demo.model.Bid;
import com.uptheant.demo.repository.BidRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    @Autowired
    public BidServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    @Override
    public Optional<Bid> getBidById(Integer id) {
        return bidRepository.findById(id);
    }

    @Override
    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public void deleteBid(Integer id) {
        bidRepository.deleteById(id);
    }
    
}
