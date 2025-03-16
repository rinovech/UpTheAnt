package com.uptheant.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.service.auction.AuctionServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Test
    void testGetAllAuctions() {
        Auction auction1 = new Auction();
        auction1.setName("Test Auction 1");

        Auction auction2 = new Auction();
        auction2.setName("Test Auction 2");

        when(auctionRepository.findAll()).thenReturn(Arrays.asList(auction1, auction2));

        List<Auction> auctions = auctionService.getAllAuctions();

        assertEquals(2, auctions.size());
        assertEquals("Test Auction 1", auctions.get(0).getName());
        assertEquals("Test Auction 2", auctions.get(1).getName());
        verify(auctionRepository, times(1)).findAll();
    }

    @Test
    void testGetAuctionById() {
        Auction auction = new Auction();
        auction.setName("Test Auction 1");

        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        Optional<Auction> foundAuction = auctionService.getAuctionById(1);

        assertTrue(foundAuction.isPresent());
        assertEquals("Test Auction 1", foundAuction.get().getName());
    }

    @Test
    void testGetAuctionByIdNotFound() {
        when(auctionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Auction> foundAuction = auctionService.getAuctionById(999);

        assertFalse(foundAuction.isPresent());
    }

    @Test
    void testCreateAuction() {
        Auction auction = new Auction();
        auction.setName("Test Auction 1");

        when(auctionRepository.save(auction)).thenReturn(auction);

        Auction createdAuction = auctionService.createAuction(auction);

        assertNotNull(createdAuction);
        assertEquals("Test Auction 1", createdAuction.getName());
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    void testDeleteAuction() {
        doNothing().when(auctionRepository).deleteById(1);

        auctionService.deleteAuction(1);

        verify(auctionRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAuctionNotFound() {
        doNothing().when(auctionRepository).deleteById(999);

        auctionService.deleteAuction(999);

        verify(auctionRepository, times(1)).deleteById(999);
    }

    @Test
    void testGetAuctionByIdWithNegativeId() {
        Optional<Auction> foundAuction = auctionService.getAuctionById(-1);

        assertFalse(foundAuction.isPresent());
    }

    @Test
    void testDeleteAuctionWithNegativeId() {
        auctionService.deleteAuction(-1);

        verify(auctionRepository, times(1)).deleteById(-1);
    }
}