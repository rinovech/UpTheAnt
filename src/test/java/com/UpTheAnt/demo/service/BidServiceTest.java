package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Bid;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BidServiceTest {

    @Test
    void testGetAllBids() {
        BidService BidService = new BidService();
        Bid Bid1 = new Bid();
        Bid1.setAuctionId(1);
        BidService.createBid(Bid1);

        Bid Bid2 = new Bid();
        Bid2.setAuctionId(2);
        BidService.createBid(Bid2);

        List<Bid> Bids = BidService.getAllBids();

        assertEquals(2, Bids.size());
        assertEquals(1, Bids.get(0).getAuctionId());
        assertEquals(2, Bids.get(1).getAuctionId());
    }

    @Test
    void testCreateBid() {
        BidService BidService = new BidService();
        Bid Bid = new Bid();
        Bid.setAuctionId(1);

        Bid createdBid = BidService.createBid(Bid);

        assertNotNull(createdBid.getBidId());
        assertEquals(1, createdBid.getAuctionId());
        assertEquals(1, BidService.getAllBids().size());
    }

    @Test
    void testDeleteBid() {
        BidService BidService = new BidService();
        Bid Bid = new Bid();
        Bid.setAuctionId(1);
        BidService.createBid(Bid);

        BidService.deleteBid(1);

        assertEquals(0, BidService.getAllBids().size());
    }

    @Test
    void testDeleteBidNotFound() {
        BidService BidService = new BidService();
        BidService.deleteBid(1000); 

        assertEquals(0, BidService.getAllBids().size());
    }

    @Test
    void testGetBidById() {
        BidService BidService = new BidService();
        Bid Bid = new Bid();
        Bid.setAuctionId(1);
        BidService.createBid(Bid);

        Optional<Bid> foundBid = BidService.getBidById(1);

        assertTrue(foundBid.isPresent());
        assertEquals(1, foundBid.get().getAuctionId());
    }

    @Test
    void testGetBidByIdNotFound() {
        BidService BidService = new BidService();
        Optional<Bid> foundBid = BidService.getBidById(1000);

        assertFalse(foundBid.isPresent());
    }

    @Test
    void testCreateBidWithInvalidData() {
        BidService BidService = new BidService();
        Bid Bid = new Bid();
        Bid.setAuctionId(0);
        Bid.setUserId(0);
        Bid.setBidAmount(null);
        Bid.setBidTime(null);

        
        Bid createdbBid = BidService.createBid(Bid);

        assertNotNull(createdbBid.getBidId());
        assertEquals(0, createdbBid.getAuctionId());
        assertEquals(0, createdbBid.getUserId());
        assertEquals(null, createdbBid.getBidAmount());
        assertEquals(null, createdbBid.getBidTime());

    }

    @Test
    void testGetBidByIdWithNegativeId() {
        BidService BidService = new BidService();
        Optional<Bid> foundBid = BidService.getBidById(-1);

        assertFalse(foundBid.isPresent());
    }

    @Test
    void testDeleteBidWithNegativeId() {
        BidService BidService = new BidService();
        BidService.deleteBid(-1);  

        assertEquals(0, BidService.getAllBids().size());
    }

}
