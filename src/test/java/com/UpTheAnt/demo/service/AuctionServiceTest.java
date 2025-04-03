package com.uptheant.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.service.auction.AuctionServiceImpl;
import com.uptheant.demo.service.mapper.AuctionMapper;
import com.uptheant.demo.service.validation.AuctionValidator;

import java.math.BigDecimal;
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

    @Mock
    private AuctionValidator auctionValidator;

    @Mock
    private AuctionMapper auctionMapper;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Test
    void testGetAllAuctions() {
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        
        AuctionResponseDTO dto1 = new AuctionResponseDTO();
        AuctionResponseDTO dto2 = new AuctionResponseDTO();

        when(auctionRepository.findAll()).thenReturn(Arrays.asList(auction1, auction2));
        when(auctionMapper.toDto(auction1)).thenReturn(dto1);
        when(auctionMapper.toDto(auction2)).thenReturn(dto2);

        List<AuctionResponseDTO> result = auctionService.getAllAuctions();

        assertEquals(2, result.size());
        verify(auctionRepository, times(1)).findAll();
        verify(auctionMapper, times(1)).toDto(auction1);
        verify(auctionMapper, times(1)).toDto(auction2);
    }

    @Test
    void testGetAuctionById() {
        Auction auction = new Auction();
        AuctionResponseDTO expectedDto = new AuctionResponseDTO();

        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        when(auctionMapper.toDto(auction)).thenReturn(expectedDto);

        AuctionResponseDTO result = auctionService.getAuctionById(1);

        assertSame(expectedDto, result);
        verify(auctionRepository, times(1)).findById(1);
        verify(auctionMapper, times(1)).toDto(auction);
    }

    @Test
    void testGetAuctionByIdNotFound() {
        when(auctionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            auctionService.getAuctionById(1);
        });
        verify(auctionRepository, times(1)).findById(1);
    }

    @Test
    void testCreateAuction() {

        AuctionCreateDTO createDTO = new AuctionCreateDTO();
        createDTO.setName("Test Auction");
        
        User seller = new User();
        seller.setUserId(1);
        
        Auction savedAuction = new Auction();
        AuctionResponseDTO expectedDto = new AuctionResponseDTO();

        when(userRepository.findById(1)).thenReturn(Optional.of(seller));
        when(auctionRepository.save(any(Auction.class))).thenReturn(savedAuction);
        when(auctionMapper.toDto(savedAuction)).thenReturn(expectedDto);

        AuctionResponseDTO result = auctionService.createAuction(createDTO, 1);

        assertSame(expectedDto, result);
        verify(userRepository).findById(1);
        verify(auctionRepository).save(any(Auction.class));
        verify(auctionMapper).toDto(savedAuction);
        
        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(auctionCaptor.capture());
        Auction createdAuction = auctionCaptor.getValue();
        
        assertEquals("Test Auction", createdAuction.getName());
        assertEquals(seller, createdAuction.getUser());
        assertFalse(createdAuction.isStatus());
    }

    @Test
    void testCloseAuction() {

        Auction auction = new Auction();
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        auctionService.closeAuction(1);

        assertTrue(auction.isStatus());
        verify(auctionValidator).validateClosing(auction);
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    void testDeleteAuctionSuccess() {

        Auction auction = new Auction();
        when(auctionRepository.existsById(1)).thenReturn(true);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        auctionService.deleteAuction(1);

        verify(auctionRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAuctionWithBids() {

        Auction auction = new Auction();
        auction.setCurrentBid(BigDecimal.TEN);
        
        when(auctionRepository.existsById(1)).thenReturn(true);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        assertThrows(BusinessRuleException.class, () -> {
            auctionService.deleteAuction(1);
        });
        verify(auctionRepository, never()).deleteById(1);
    }

    @Test
    void testDeleteAuctionNotFound() {

        when(auctionRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            auctionService.deleteAuction(1);
        });
        verify(auctionRepository, never()).deleteById(1);
    }
}