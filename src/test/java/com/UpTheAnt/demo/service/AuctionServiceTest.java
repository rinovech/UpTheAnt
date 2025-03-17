package com.uptheant.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.service.auction.AuctionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Test
    void testGetAllAuctions() {

        User seller = new User();
        seller.setUserId(1);

        Auction auction1 = new Auction();
        auction1.setName("Test Auction 1");
        auction1.setDescription("test auction description");
        auction1.setStartPrice(BigDecimal.valueOf(100.0));
        auction1.setMinBidStep(BigDecimal.valueOf(10.0));
        auction1.setStartTime(LocalDateTime.now());
        auction1.setEndTime(LocalDateTime.now().plusDays(7));
        auction1.setUser(seller); 

        Auction auction2 = new Auction();
        auction2.setName("Test Auction 2");
        auction2.setDescription("test auction description");
        auction2.setStartPrice(BigDecimal.valueOf(100.0));
        auction2.setMinBidStep(BigDecimal.valueOf(10.0));
        auction2.setStartTime(LocalDateTime.now());
        auction2.setEndTime(LocalDateTime.now().plusDays(7));
        auction2.setUser(seller); 

        when(auctionRepository.findAll()).thenReturn(Arrays.asList(auction1, auction2));

        List<AuctionResponseDTO> auctions = auctionService.getAllAuctions();

        assertEquals(2, auctions.size());
        assertEquals("Test Auction 1", auctions.get(0).getName());
        assertEquals("Test Auction 2", auctions.get(1).getName());
        verify(auctionRepository, times(1)).findAll();
    }

    @Test
    void testGetAuctionById() {

        User seller = new User();
        seller.setUserId(1);

        Auction auction = new Auction();
        auction.setName("Test Auction 1");
        auction.setDescription("test auction description");
        auction.setStartPrice(BigDecimal.valueOf(100.0));
        auction.setMinBidStep(BigDecimal.valueOf(10.0));
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusDays(7));
        auction.setUser(seller); 

        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        AuctionResponseDTO foundAuction = auctionService.getAuctionById(1);

        assertNotNull(foundAuction);
        assertEquals("Test Auction 1", foundAuction.getName());
        verify(auctionRepository, times(1)).findById(1);
    }

    @Test
    void testGetAuctionByIdNotFound() {
        when(auctionRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            auctionService.getAuctionById(999);
        });
        
        assertEquals("Auction not found", exception.getMessage());
        verify(auctionRepository, times(1)).findById(999);
    }

    @Test
    void testCreateAuction() {

        AuctionCreateDTO auctionCreateDTO = new AuctionCreateDTO();
        auctionCreateDTO.setName("Test Auction 1");
        auctionCreateDTO.setDescription("test auction description");
        auctionCreateDTO.setStartPrice(new BigDecimal("100.00"));
        auctionCreateDTO.setMinBidStep(new BigDecimal("10.00"));
        auctionCreateDTO.setStartTime(LocalDateTime.now());
        auctionCreateDTO.setEndTime(LocalDateTime.now().plusDays(7));

        User seller = new User();
        seller.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(seller));

        Auction auction = new Auction();
        auction.setName(auctionCreateDTO.getName());
        auction.setDescription(auctionCreateDTO.getDescription());
        auction.setStartPrice(auctionCreateDTO.getStartPrice());
        auction.setMinBidStep(auctionCreateDTO.getMinBidStep());
        auction.setStartTime(auctionCreateDTO.getStartTime());
        auction.setEndTime(auctionCreateDTO.getEndTime());
        auction.setUser(seller); 

        when(auctionRepository.save(any(Auction.class))).thenReturn(auction);

        AuctionResponseDTO createdAuction = auctionService.createAuction(auctionCreateDTO, 1);

        assertNotNull(createdAuction);
        assertEquals("Test Auction 1", createdAuction.getName());
        assertEquals("test auction description", createdAuction.getDescription());
        assertEquals(new BigDecimal("100.00"), createdAuction.getStartPrice());
        assertEquals(new BigDecimal("10.00"), createdAuction.getMinBidStep());
        assertFalse(createdAuction.isStatus());
        assertEquals(1, createdAuction.getSellerId());
    
        verify(userRepository, times(1)).findById(1); 
        verify(auctionRepository, times(1)).save(any(Auction.class));
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

}