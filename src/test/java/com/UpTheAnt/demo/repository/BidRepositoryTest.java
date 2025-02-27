package com.UpTheAnt.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.UpTheAnt.demo.model.Bid;
import com.UpTheAnt.demo.model.Auction;
import com.UpTheAnt.demo.model.User;

@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})

public class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    private Bid testBid;

    @Autowired
    private AuctionRepository auctionRepository;

    private Auction testAuction;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setName("Veronika");
        testUser.setUsername("veronika123");
        testUser.setEmail("veronika@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        testAuction = new Auction();
        testAuction.setName("Test Auction");
        testAuction.setDescription("test auction description"); 
        testAuction.setStartPrice(new BigDecimal("100.00")); 
        testAuction.setMinBidStep(new BigDecimal("10.00")); 
        testAuction.setStartTime(LocalDateTime.now()); 
        testAuction.setEndTime(LocalDateTime.now().plusDays(7)); 
        testAuction.setCurrentBid(new BigDecimal("100.00")); 
        testAuction.setStatus(true); 
        testAuction.setUser(testUser);
        auctionRepository.save(testAuction);

        testBid = new Bid();
        testBid.setBidAmount(new BigDecimal("10.0"));
        testBid.setBidTime(LocalDateTime.now());
        testBid.setAuction(testAuction);
        testBid.setUser(testUser);
        bidRepository.save(testBid);
    }

    @AfterEach
    public void tearDown() {

        bidRepository.delete(testBid);
        auctionRepository.delete(testAuction);
        userRepository.delete(testUser);
    }

    @Test
    void givenBid_whenSaved_thenCanBeFoundById() {

        Bid savedBid = bidRepository.findById(testBid.getBidId())
            .orElse(null);

        assertNotNull(savedBid);
        assertEquals(testBid.getBidAmount(), savedBid.getBidAmount());
        assertEquals(testBid.getBidTime(), savedBid.getBidTime());
        assertEquals(testAuction.getAuctionId(), savedBid.getAuction().getAuctionId());
        assertEquals(testUser.getUserId(), savedBid.getUser().getUserId());
    }

    @Test
    void givenBid_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        testBid.setBidAmount(new BigDecimal("50.0"));
        bidRepository.save(testBid);

        Bid updatedBid = bidRepository.findById(testBid.getBidId())
            .orElse(null);

        assertNotNull(updatedBid);
        assertEquals(new BigDecimal("50.0"), updatedBid.getBidAmount());
    }

}