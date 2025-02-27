package com.UpTheAnt.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.UpTheAnt.demo.model.Auction;
import com.UpTheAnt.demo.model.User;

@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})

public class AuctionRepositoryTest {


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
    }

    @AfterEach
    public void tearDown() {

        auctionRepository.delete(testAuction);
        userRepository.delete(testUser);
    }

    @Test
    void givenAuction_whenSaved_thenCanBeFoundById() {

        Auction savedAuction = auctionRepository.findById(testAuction.getAuctionId())
            .orElse(null);

        assertNotNull(savedAuction.getAuctionId());
        assertEquals("Test Auction", savedAuction.getName());
        assertEquals("test auction description", savedAuction.getDescription());
        assertEquals(new BigDecimal("100.00"), savedAuction.getStartPrice());
        assertEquals(new BigDecimal("10.00"), savedAuction.getMinBidStep());
        assertNotNull(savedAuction.getStartTime());
        assertNotNull(savedAuction.getEndTime());
        assertEquals(new BigDecimal("100.00"), savedAuction.getCurrentBid());
        assertTrue(savedAuction.isStatus());
        assertEquals(testUser.getUserId(), savedAuction.getUser().getUserId());
    }

    @Test
    void givenAuction_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        testAuction.setName("update name");
        auctionRepository.save(testAuction);

        Auction updatedAuction = auctionRepository.findById(testAuction.getAuctionId())
            .orElse(null);

        assertNotNull(updatedAuction);
        assertEquals("update name", updatedAuction.getName());
    }

}