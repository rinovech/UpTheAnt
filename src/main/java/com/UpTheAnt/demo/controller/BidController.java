package com.UpTheAnt.demo.controller;

import com.UpTheAnt.demo.model.Bid;
import com.UpTheAnt.demo.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }

    @GetMapping("/{id}")
    public Optional<Bid> getBidById(@PathVariable Integer id) {
        return bidService.getBidById(id);
    }

    @PostMapping
    public Bid createBid(@RequestBody Bid bid) {
        return bidService.createBid(bid);
    }

    @DeleteMapping("/{id}")
    public void deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
    }
}