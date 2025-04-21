package com.uptheant.demo.validation;

import com.uptheant.demo.DemoApplicationTests;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BidValidationTest extends DemoApplicationTests {

    @Autowired
    private BidController bidController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    private User testUser;
    private Auction testAuction;
    private BigDecimal startPrice = BigDecimal.valueOf(100);
    private BigDecimal minBidStep = BigDecimal.valueOf(10);

    @BeforeEach
    void setUp() {

        testUser = User.builder()
            .name("Test User")
            .username("testuser" + UUID.randomUUID().toString().substring(0, 8))
            .email("test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com")
            .password("TestPassword123!")
            .build();

        testUser = userRepository.save(testUser);

        testAuction = Auction.builder()
            .name("Test Auction")
            .description("Test Description")
            .startPrice(startPrice)
            .minBidStep(minBidStep)
            .startTime(LocalDateTime.now().minusDays(1))  
            .endTime(LocalDateTime.now().plusDays(7))
            .user(testUser) 
            .status(true)
            .currentBid(startPrice) 
            .build();

        testAuction = auctionRepository.save(testAuction);
    }

    private BidCreateDTO createValidBid() {
        BidCreateDTO bid = new BidCreateDTO();
        bid.setBidAmount(startPrice.add(minBidStep));
        return bid;
    }

    @Test
    void createBid_withValidData_shouldSucceed() {
        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createBid_withNonExistentAuction_shouldFail() {
        BidCreateDTO validBid = createValidBid();
        Integer invalidAuctionId = testAuction.getAuctionId() + 999;

        ResponseEntity<?> response = bidController.createBid(validBid, testUser.getUserId(), invalidAuctionId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBid_withNonExistentUser_shouldFail() {
        BidCreateDTO validBid = createValidBid();
        Integer invalidUserId = testUser.getUserId() + 999;

        ResponseEntity<?> response = bidController.createBid(validBid, invalidUserId, testAuction.getAuctionId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_onInactiveAuction_shouldFail() {
        testAuction.setStatus(false);
        auctionRepository.save(testAuction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_beforeAuctionStart_shouldFail() {
        testAuction.setStartTime(LocalDateTime.now().plusHours(1));
        auctionRepository.save(testAuction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_afterAuctionEnd_shouldFail() {
        testAuction.setEndTime(LocalDateTime.now().minusHours(1));
        auctionRepository.save(testAuction);

        BidCreateDTO validBid = createValidBid();

        ResponseEntity<?> response = bidController.createBid(validBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_withAmountLessThanRequired_shouldFail() {
        BidCreateDTO invalidBid = new BidCreateDTO();
        invalidBid.setBidAmount(startPrice.add(minBidStep).subtract(BigDecimal.ONE));

        ResponseEntity<?> response = bidController.createBid(invalidBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createBid_withConsecutiveValidBids_shouldUpdateCurrentBid() {
        BidCreateDTO firstBid = new BidCreateDTO();
        firstBid.setBidAmount(startPrice.add(minBidStep));
        ResponseEntity<?> firstResponse = bidController.createBid(firstBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());

        BidCreateDTO secondBid = new BidCreateDTO();
        secondBid.setBidAmount(firstBid.getBidAmount().add(minBidStep));
        ResponseEntity<?> secondResponse = bidController.createBid(secondBid, testUser.getUserId(), testAuction.getAuctionId());
        assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());

        Auction updatedAuction = auctionRepository.findById(testAuction.getAuctionId()).orElseThrow();
        assertEquals(secondBid.getBidAmount(), updatedAuction.getCurrentBid());
    }
}