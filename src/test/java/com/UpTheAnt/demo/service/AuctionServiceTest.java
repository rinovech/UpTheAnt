package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Auction;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionServiceTest {

    @Test
    void testGetAllAuctions() {
        AuctionService AuctionService = new AuctionService();
        Auction Auction1 = new Auction();
        Auction1.setName("Test Auction 1");
        AuctionService.createAuction(Auction1);

        Auction Auction2 = new Auction();
        Auction2.setName("Test Auction 2");
        AuctionService.createAuction(Auction2);

        List<Auction> Auctions = AuctionService.getAllAuctions();

        assertEquals(2, Auctions.size());
        assertEquals("Test Auction 1", Auctions.get(0).getName());
        assertEquals("Test Auction 2", Auctions.get(1).getName());
    }

    @Test
    void testGetAuctionById() {
        AuctionService AuctionService = new AuctionService();
        Auction Auction = new Auction();
        Auction.setName("Test Auction 1");
        AuctionService.createAuction(Auction);

        Optional<Auction> foundAuction = AuctionService.getAuctionById(1);

        assertTrue(foundAuction.isPresent());
        assertEquals("Test Auction 1", foundAuction.get().getName());
    }

    @Test
    void testGetAuctionByIdNotFound() {
        AuctionService AuctionService = new AuctionService();
        Optional<Auction> foundAuction = AuctionService.getAuctionById(999);

        assertFalse(foundAuction.isPresent());
    }

    @Test
    void testCreateAuction() {
        AuctionService AuctionService = new AuctionService();
        Auction Auction = new Auction();
        Auction.setName("Test Auction 1");

        Auction createdAuction = AuctionService.createAuction(Auction);

        assertNotNull(createdAuction.getAuctionId());
        assertEquals("Test Auction 1", createdAuction.getName());
        assertEquals(1, AuctionService.getAllAuctions().size());
    }

    @Test
    void testDeleteAuction() {
        AuctionService AuctionService = new AuctionService();
        Auction Auction = new Auction();
        Auction.setName("Test Auction 1");
        AuctionService.createAuction(Auction);

        AuctionService.deleteAuction(1);

        assertEquals(0, AuctionService.getAllAuctions().size());
    }

    @Test
    void testDeleteAuctionNotFound() {
        AuctionService AuctionService = new AuctionService();
        AuctionService.deleteAuction(999); 

        assertEquals(0, AuctionService.getAllAuctions().size());
    }

    @Test
    void testCreateAuctionWithInvalidData() {
        AuctionService AuctionService = new AuctionService();
        Auction Auction = new Auction();
        Auction.setName(""); 
        Auction.setDescription(""); 
        Auction.setMinBidStep(new BigDecimal("0.0"));
        Auction.setStartPrice(new BigDecimal("0.0"));
        Auction.setStartTime(null);
        Auction.setEndTime(null);
        Auction.setCurrentBid(new BigDecimal("0.0"));
        Auction.setStatus(false);
        Auction.setUserId(null);


        Auction createdAuction = AuctionService.createAuction(Auction);

        assertNotNull(createdAuction.getAuctionId());
        assertEquals("", createdAuction.getName());
        assertEquals("", createdAuction.getDescription());
        assertEquals(new BigDecimal("0.0"), createdAuction.getMinBidStep());
        assertEquals(new BigDecimal("0.0"), createdAuction.getStartPrice());
        assertEquals(null, createdAuction.getStartTime());
        assertEquals(null, createdAuction.getEndTime());
        assertEquals(new BigDecimal("0.0"), createdAuction.getCurrentBid());
        assertEquals(false, createdAuction.isStatus());
        assertEquals(null, createdAuction.getUserId());

    }

    @Test
    void testGetAuctionByIdWithNegativeId() {
        AuctionService AuctionService = new AuctionService();
        Optional<Auction> foundAuction = AuctionService.getAuctionById(-1);

        assertFalse(foundAuction.isPresent());
    }

    @Test
    void testDeleteAuctionWithNegativeId() {
        AuctionService AuctionService = new AuctionService();
        AuctionService.deleteAuction(-1); 

        assertEquals(0, AuctionService.getAllAuctions().size());
    }
}
