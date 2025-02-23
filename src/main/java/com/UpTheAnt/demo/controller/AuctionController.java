package com.UpTheAnt.demo.controller;

import com.UpTheAnt.demo.model.Auction;
import com.UpTheAnt.demo.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("/{id}")
    public Optional<Auction> getAuctionById(@PathVariable Integer id) {
        return auctionService.getAuctionById(id);
    }

    @PostMapping
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @DeleteMapping("/{id}")
    public void deleteAuction(@PathVariable Integer id) {
        auctionService.deleteAuction(id);
    }
}
