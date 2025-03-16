package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.model.Bid;
import com.uptheant.demo.service.bid.BidService;

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
    public ResponseEntity<Bid> getBidById(@PathVariable Integer id) {
        Optional<Bid> bid = bidService.getBidById(id);
        return bid.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bid createBid(@RequestBody Bid bid) {
        return bidService.createBid(bid);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
        return ResponseEntity.ok("Bid with ID " + id + " was successfully deleted.");
    }
}