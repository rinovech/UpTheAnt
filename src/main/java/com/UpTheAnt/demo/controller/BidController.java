package com.UpTheAnt.demo.controller;

import com.UpTheAnt.demo.model.Bid;
import com.UpTheAnt.demo.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
    }
}