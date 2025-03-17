package com.uptheant.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.service.bid.BidService;

import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }


    @GetMapping
    public List<BidResponseDTO> getAllBids() {
        return bidService.getAllBids();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidResponseDTO> getBidById(@PathVariable Integer id) {
        try {
            BidResponseDTO bid = bidService.getBidById(id);
            return ResponseEntity.ok(bid);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BidResponseDTO createBid(@RequestBody BidCreateDTO bidCreateDTO, @RequestParam Integer userId, @RequestParam Integer auctionId) {
        return bidService.createBid(bidCreateDTO, userId, auctionId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBid(@PathVariable Integer id) {
        bidService.deleteBid(id);
        return ResponseEntity.ok("Bid with ID " + id + " was successfully deleted.");
    }
}