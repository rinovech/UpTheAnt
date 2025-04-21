package com.uptheant.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.service.bid.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BidControllerTest {

    @Mock
    private BidService bidService;

    @InjectMocks
    private BidController bidController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllBids_shouldReturnListOfBids() throws Exception {
        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setBidAmount(BigDecimal.valueOf(100.0));
        bidResponseDTO.setBidTime(LocalDateTime.now());
        bidResponseDTO.setUserId(1);
        bidResponseDTO.setAuctionId(1);

        List<BidResponseDTO> bids = Collections.singletonList(bidResponseDTO);

        when(bidService.getAllBids()).thenReturn(bids);

        mockMvc.perform(get("/api/bids"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bidAmount").value(100.0))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].auctionId").value(1));
    }
    
    @Test
    public void getBidById_whenBidExists_shouldReturnBid() throws Exception {

        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setBidAmount(BigDecimal.valueOf(100.0));
        bidResponseDTO.setBidTime(LocalDateTime.now());
        bidResponseDTO.setUserId(1);
        bidResponseDTO.setAuctionId(1);

        when(bidService.getBidById(1)).thenReturn(bidResponseDTO);

        mockMvc.perform(get("/api/bids/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidAmount").value(100.0))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.auctionId").value(1));
    }

    @Test
    public void getBidById_whenBidDoesNotExist_shouldReturn404() throws Exception {

        when(bidService.getBidById(1)).thenThrow(new RuntimeException("Bid not found"));

        mockMvc.perform(get("/api/bids/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBid_shouldReturnCreatedBid() throws Exception {

        BidCreateDTO bidCreateDTO = new BidCreateDTO();
        bidCreateDTO.setBidAmount(BigDecimal.valueOf(100.0));

        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setBidAmount(BigDecimal.valueOf(100.0));
        bidResponseDTO.setBidTime(LocalDateTime.now());
        bidResponseDTO.setUserId(1);
        bidResponseDTO.setAuctionId(1);

        when(bidService.createBid(any(BidCreateDTO.class), eq(1), eq(1))).thenReturn(bidResponseDTO);

        mockMvc.perform(post("/api/bids")
                .param("userId", "1")
                .param("auctionId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bidCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bidAmount").value(100.0))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.auctionId").value(1));
    }

    @Test
    public void deleteBid_shouldReturnSuccessMessage() throws Exception {

        doNothing().when(bidService).deleteBid(1);

        mockMvc.perform(delete("/api/bids/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid with ID 1 was successfully deleted."));
    }
}