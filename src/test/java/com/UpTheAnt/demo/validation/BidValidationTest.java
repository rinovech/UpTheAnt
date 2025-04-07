package com.uptheant.demo.validation;

import com.uptheant.demo.controller.BidController;
import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BidValidationTest {

    @Autowired
    private BidController bidController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    private Integer userId;
    private Integer auctionId;
    private BigDecimal startPrice = BigDecimal.valueOf(100);
    private BigDecimal minBidStep = BigDecimal.valueOf(10);

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("Password123!");

        User savedUser = userRepository.save(user);

        this.userId = savedUser.getUserId();

        Auction auction = new Auction();
        auction.setName("Test Auction");
        auction.setDescription("Auction for testing bids");
        auction.setStartPrice(startPrice);
        auction.setMinBidStep(minBidStep);
        auction.setStartTime(LocalDateTime.now().minusHours(1));
        auction.setStatus(true);
        auction.setEndTime(LocalDateTime.now().plusHours(2));

        Auction savedaAuction = auctionRepository.save(auction);

        this.auctionId = savedaAuction.getAuctionId();
    }

    private BidCreateDTO createValidBid() {
        BidCreateDTO bid = new BidCreateDTO();
        bid.setBidAmount(startPrice.add(minBidStep));
        return bid;
    }

    @Test
    void createBid_withValidData_shouldSucceed() {
        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, userId, auctionId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createBid_withNonExistentAuction_shouldFail() {
        BidCreateDTO validBid = createValidBid();
        Integer invalidAuctionId = auctionId + 999;

        ResponseEntity<?> response = bidController.createBid(validBid, userId, invalidAuctionId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Auction not found", response.getBody());
    }

    @Test
    void createBid_withNonExistentUser_shouldFail() {
        BidCreateDTO validBid = createValidBid();
        Integer invalidUserId = userId + 999;

        ResponseEntity<?> response = bidController.createBid(validBid, invalidUserId, auctionId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void createBid_onInactiveAuction_shouldFail() {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        auction.setStatus(false);
        auctionRepository.save(auction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, userId, auctionId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Auction is not active", response.getBody());
    }

    @Test
    void createBid_beforeAuctionStart_shouldFail() {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        auction.setStartTime(LocalDateTime.now().plusHours(1));
        auction.setEndTime(LocalDateTime.now().plusHours(3));
        auctionRepository.save(auction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, userId, auctionId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Auction has not started yet", response.getBody());
    }

    @Test
    void createBid_afterAuctionEnd_shouldFail() {

        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        auction.setStartTime(LocalDateTime.now().minusHours(2));
        auction.setEndTime(LocalDateTime.now().minusHours(1));
        auctionRepository.save(auction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, userId, auctionId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Auction has already ended", response.getBody());
    }

    @Test
    void createBid_withAmountLessThanRequired_shouldFail() {
        BidCreateDTO invalidBid = new BidCreateDTO();
        invalidBid.setBidAmount(startPrice.add(minBidStep).subtract(BigDecimal.ONE));

        ResponseEntity<?> response = bidController.createBid(invalidBid, userId, auctionId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_withConsecutiveValidBids_shouldUpdateCurrentBid() {

        BidCreateDTO firstBid = new BidCreateDTO();
        firstBid.setBidAmount(startPrice.add(minBidStep));
        ResponseEntity<?> firstResponse = bidController.createBid(firstBid, userId, auctionId);
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());

        BidCreateDTO secondBid = new BidCreateDTO();
        secondBid.setBidAmount(firstBid.getBidAmount().add(minBidStep));
        ResponseEntity<?> secondResponse = bidController.createBid(secondBid, userId, auctionId);
        assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());

        Auction updatedAuction = auctionRepository.findById(auctionId).orElseThrow();
        assertEquals(secondBid.getBidAmount(), updatedAuction.getCurrentBid());
    }
}
