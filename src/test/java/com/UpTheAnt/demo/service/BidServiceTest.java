package com.uptheant.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.bid.BidServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    @Test
    void testGetAllBids() {

        User user = new User();
        user.setUserId(1);

        Auction auction = new Auction();
        auction.setAuctionId(1);

        Bid bid1 = new Bid();
        bid1.setBidAmount(new BigDecimal("10.0"));
        bid1.setUser(user);
        bid1.setAuction(auction);
        bid1.setBidTime(LocalDateTime.now());

        Bid bid2 = new Bid();
        bid2.setBidAmount(new BigDecimal("50.0"));
        bid2.setUser(user);
        bid2.setAuction(auction);
        bid2.setBidTime(LocalDateTime.now());

        when(bidRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        List<BidResponseDTO> bids = bidService.getAllBids();

        assertEquals(2, bids.size());
        assertEquals(new BigDecimal("10.0"), bids.get(0).getBidAmount());
        assertEquals(new BigDecimal("50.0"), bids.get(1).getBidAmount());
        verify(bidRepository, times(1)).findAll();
    }

    @Test
    void testGetBidById() {

        User user = new User();
        user.setUserId(1);

        Auction auction = new Auction();
        auction.setAuctionId(1);

        Bid bid1 = new Bid();
        bid1.setBidAmount(new BigDecimal("10.0"));
        bid1.setUser(user);
        bid1.setAuction(auction);
        bid1.setBidTime(LocalDateTime.now());

        when(bidRepository.findById(1)).thenReturn(Optional.of(bid1));

        BidResponseDTO foundBid = bidService.getBidById(1);

        assertNotNull(foundBid);
        assertEquals(new BigDecimal("10.0"), foundBid.getBidAmount());
        verify(bidRepository, times(1)).findById(1);
    }

    @Test
    void testGetBidByIdNotFound() {

        when(bidRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bidService.getBidById(999);
        });
        
        assertEquals("Bid not found", exception.getMessage());

        verify(bidRepository, times(1)).findById(999);
    }

    @Test
    void testCreateBid() {

        BidCreateDTO bidCreateDTO = new BidCreateDTO();
        bidCreateDTO.setBidAmount(new BigDecimal("10.0"));

        User user = new User();
        user.setUserId(1);

        Auction auction = new Auction();
        auction.setAuctionId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        Bid bid = new Bid();
        bid.setBidAmount(bidCreateDTO.getBidAmount());
        bid.setBidTime(LocalDateTime.now());
        bid.setAuction(auction);
        bid.setUser(user);

        when(bidRepository.save(any(Bid.class))).thenReturn(bid);

        BidResponseDTO createdBid = bidService.createBid(bidCreateDTO, 1, 1);

        assertNotNull(createdBid);
        assertEquals(new BigDecimal("10.0"), createdBid.getBidAmount());
        assertEquals(1, createdBid.getUserId());
        assertEquals(1, createdBid.getAuctionId());
    
        verify(userRepository, times(1)).findById(1); 
        verify(auctionRepository, times(1)).findById(1);
        verify(bidRepository, times(1)).save(any(Bid.class));
    }

    @Test
    void testDeleteBid() {

        doNothing().when(bidRepository).deleteById(1);

        bidService.deleteBid(1);

        verify(bidRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBidNotFound() {

        doNothing().when(bidRepository).deleteById(1000);

        bidService.deleteBid(1000);

        verify(bidRepository, times(1)).deleteById(1000);
    }

}