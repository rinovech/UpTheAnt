package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.Bid;
import com.UpTheAnt.demo.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private BidService bidService;

    @Test
    void testGetAllBids() {

        Bid bid1 = new Bid();
        bid1.setBidAmount(new BigDecimal("10.0"));

        Bid bid2 = new Bid();
        bid2.setBidAmount(new BigDecimal("50.0"));

        when(bidRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        List<Bid> bids = bidService.getAllBids();

        assertEquals(2, bids.size());
        assertEquals(new BigDecimal("10.0"), bids.get(0).getBidAmount());
        assertEquals(new BigDecimal("50.0"), bids.get(1).getBidAmount());
        verify(bidRepository, times(1)).findAll();
    }

    @Test
    void testCreateBid() {

        Bid bid = new Bid();
        bid.setBidAmount(new BigDecimal("10.0"));

        when(bidRepository.save(bid)).thenReturn(bid);

        Bid createdBid = bidService.createBid(bid);

        assertNotNull(createdBid);
        assertEquals(new BigDecimal("10.0"), createdBid.getBidAmount());
        verify(bidRepository, times(1)).save(bid);
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

    @Test
    void testGetBidById() {

        Bid bid = new Bid();
        bid.setBidAmount(new BigDecimal("10.0"));

        when(bidRepository.findById(1)).thenReturn(Optional.of(bid));

        // Act
        Optional<Bid> foundBid = bidService.getBidById(1);

        // Assert
        assertTrue(foundBid.isPresent());
        assertEquals(new BigDecimal("10.0"), foundBid.get().getBidAmount());
        verify(bidRepository, times(1)).findById(1);
    }

    @Test
    void testGetBidByIdNotFound() {
        // Arrange
        when(bidRepository.findById(1000)).thenReturn(Optional.empty());

        // Act
        Optional<Bid> foundBid = bidService.getBidById(1000);

        // Assert
        assertFalse(foundBid.isPresent());
        verify(bidRepository, times(1)).findById(1000);
    }

    // @Test
    // void testCreateBidWithInvalidData() {
    //     // Arrange
    //     Bid bid = new Bid();
    //     bid.setBidAmount(null);
    //     bid.setBidTime(null);

    //     when(bidRepository.save(bid)).thenReturn(bid);

    //     // Act
    //     Bid createdBid = bidService.createBid(bid);

    //     // Assert
    //     assertNotNull(createdBid.getBidId());
    //     assertNull(createdBid.getBidAmount());
    //     assertNull(createdBid.getBidTime());
    //     verify(bidRepository, times(1)).save(bid);
    // }

    @Test
    void testGetBidByIdWithNegativeId() {

        when(bidRepository.findById(-1)).thenReturn(Optional.empty());

        Optional<Bid> foundBid = bidService.getBidById(-1);

        assertFalse(foundBid.isPresent());
        verify(bidRepository, times(1)).findById(-1);
    }

    @Test
    void testDeleteBidWithNegativeId() {

        doNothing().when(bidRepository).deleteById(-1);

        bidService.deleteBid(-1);

        verify(bidRepository, times(1)).deleteById(-1);
    }
}