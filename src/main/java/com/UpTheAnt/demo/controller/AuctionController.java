package com.UpTheAnt.demo.controller;

import com.UpTheAnt.demo.model.Auction;
import com.UpTheAnt.demo.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Auction> getAuctionById(@PathVariable Integer id) {
        Optional<Auction> auction = auctionService.getAuctionById(id);
        return auction.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuction(@PathVariable Integer id) {
        auctionService.deleteAuction(id);
    }
}
