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
import com.uptheant.demo.service.mapper.BidMapper;
import com.uptheant.demo.service.validation.BidValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock private BidRepository bidRepository;
    @Mock private AuctionRepository auctionRepository;
    @Mock private UserRepository userRepository;
    @Mock private BidValidator bidValidator;
    @Mock private BidMapper bidMapper;
    
    @InjectMocks private BidServiceImpl bidService;

    private Bid createTestBid(Integer id, BigDecimal amount, Integer userId, Integer auctionId) {
        Bid bid = new Bid();
        bid.setBidId(id);
        bid.setBidAmount(amount);
        bid.setBidTime(LocalDateTime.now());
        
        User user = new User();
        user.setUserId(userId);
        bid.setUser(user);
        
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        bid.setAuction(auction);
        
        return bid;
    }

    @Test
    void testCreateBid_Success() {

        BidCreateDTO createDTO = new BidCreateDTO();
        createDTO.setBidAmount(new BigDecimal("150.00"));
        
        Integer userId = 1;
        Integer auctionId = 1;

        User user = new User();
        user.setUserId(userId);
        
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auction.setStartPrice(new BigDecimal("100.00"));
        auction.setMinBidStep(new BigDecimal("10.00"));
        
        Bid newBid = new Bid();
        newBid.setBidAmount(createDTO.getBidAmount());
        newBid.setBidTime(LocalDateTime.now());
        
        Bid savedBid = new Bid();
        savedBid.setBidId(1);
        savedBid.setBidAmount(new BigDecimal("150.00"));
        savedBid.setBidTime(LocalDateTime.now());
        savedBid.setUser(user);
        savedBid.setAuction(auction);
        
        BidResponseDTO expectedDto = new BidResponseDTO();
        expectedDto.setBidAmount(new BigDecimal("150.00"));
        expectedDto.setBidTime(savedBid.getBidTime());
        expectedDto.setUserId(userId);
        expectedDto.setAuctionId(auctionId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(bidMapper.toEntity(createDTO)).thenReturn(newBid);
        when(bidRepository.save(any(Bid.class))).thenReturn(savedBid);
        when(bidMapper.toDto(savedBid)).thenReturn(expectedDto);

        BidResponseDTO result = bidService.createBid(createDTO, userId, auctionId);

        assertNotNull(result);
        assertEquals(new BigDecimal("150.00"), result.getBidAmount());
        assertEquals(userId, result.getUserId());
        assertEquals(auctionId, result.getAuctionId());

        verify(bidValidator).validateBidCreation(createDTO, userId, auctionId);
        verify(userRepository).findById(userId);
        verify(auctionRepository).findById(auctionId);
        verify(bidRepository).save(any(Bid.class));
        verify(bidMapper).toDto(savedBid);
    }

    @Test
    void testGetAllBids() {

        Bid bid1 = createTestBid(1, new BigDecimal("100.00"), 1, 1);
        Bid bid2 = createTestBid(2, new BigDecimal("150.00"), 2, 1);
        
        BidResponseDTO dto1 = new BidResponseDTO();
        dto1.setBidAmount(new BigDecimal("100.00"));
        
        BidResponseDTO dto2 = new BidResponseDTO();
        dto2.setBidAmount(new BigDecimal("150.00"));
        
        when(bidRepository.findAll()).thenReturn(List.of(bid1, bid2));
        when(bidMapper.toDto(bid1)).thenReturn(dto1);
        when(bidMapper.toDto(bid2)).thenReturn(dto2);

        List<BidResponseDTO> result = bidService.getAllBids();

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("100.00"), result.get(0).getBidAmount());
        assertEquals(new BigDecimal("150.00"), result.get(1).getBidAmount());
    }

    @Test
    void testDeleteBid_Success() {

        Integer bidId = 1;
        when(bidRepository.existsById(bidId)).thenReturn(true);
 
        bidService.deleteBid(bidId);

        verify(bidRepository).deleteById(bidId);
    }
}