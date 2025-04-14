package com.uptheant.demo.validation;

import com.uptheant.demo.DemoApplicationTests;
import com.uptheant.demo.controller.AuctionController;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuctionValidationTest extends DemoApplicationTests {

    @Autowired
    private AuctionController auctionController;

    @Autowired
    private UserRepository userRepository;

    private Integer userId;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setName("Veronika");
        user.setUsername("veronika123");
        user.setEmail("veronika@example.com");
        user.setPassword("Password123!");

        User savedUser = userRepository.save(user);

        this.userId = savedUser.getUserId();
    }

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll(); 
    }

    private AuctionCreateDTO createBaseAuction() {

        AuctionCreateDTO dto = new AuctionCreateDTO();
        dto.setName("Valid Auction");
        dto.setDescription("Description");
        dto.setStartPrice(BigDecimal.valueOf(100));
        dto.setMinBidStep(BigDecimal.valueOf(10));
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));
        return dto;
    }

    @Test
    void createAuction_withValidData_shouldReturnCreated() {

        AuctionCreateDTO validAuction = createBaseAuction();

        ResponseEntity<?> response = auctionController.createAuction(validAuction, userId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createAuction_withNullTimes_shouldReturnBadRequest() {
        AuctionCreateDTO invalidAuction = createBaseAuction();
        invalidAuction.setStartTime(null);
        invalidAuction.setEndTime(null);

        ResponseEntity<?> response = auctionController.createAuction(invalidAuction, userId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Start and end times must be specified", response.getBody());
    }

    @Test
    void createAuction_withStartAfterEnd_shouldReturnBadRequest() {
        AuctionCreateDTO invalidAuction = createBaseAuction();
        invalidAuction.setStartTime(LocalDateTime.now().plusHours(2));
        invalidAuction.setEndTime(LocalDateTime.now().plusHours(1));

        ResponseEntity<?> response = auctionController.createAuction(invalidAuction, userId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Start time must be before end time", response.getBody());
    }

    @Test
    void createAuction_withNonPositiveStartPrice_shouldReturnBadRequest() {
        AuctionCreateDTO invalidAuction = createBaseAuction();
        invalidAuction.setStartPrice(BigDecimal.valueOf(0));

        ResponseEntity<?> response = auctionController.createAuction(invalidAuction, userId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Start price must be positive", response.getBody());
    }

    @Test
    void createAuction_withNonPositiveBidStep_shouldReturnBadRequest() {
        AuctionCreateDTO invalidAuction = createBaseAuction();
        invalidAuction.setMinBidStep(BigDecimal.valueOf(-1));

        ResponseEntity<?> response = auctionController.createAuction(invalidAuction, userId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Minimum bid step must be positive", response.getBody());
    }
}