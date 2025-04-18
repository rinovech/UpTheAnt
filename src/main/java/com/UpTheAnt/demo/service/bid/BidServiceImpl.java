package com.uptheant.demo.service.bid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.BidMapper;
import com.uptheant.demo.service.validation.BidValidator;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidValidator bidValidator;
    private final BidMapper bidMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BidResponseDTO> getAllBids() {
        return bidRepository.findAll().stream()
                .map(bidMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BidResponseDTO getBidById(Integer id) {
        return bidRepository.findById(id)
                .map(bidMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Bid not found with ID: " + id));
    }

    @Override
    @Transactional
    public BidResponseDTO createBid(BidCreateDTO bidCreateDTO, Integer userId, Integer auctionId) {

        bidValidator.validateBidCreation(bidCreateDTO, userId, auctionId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found"));

        Bid bid = bidMapper.toEntity(bidCreateDTO);
        if (bid == null) {
            throw new IllegalArgumentException("Bid cannot be null");
        }
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setBidTime(LocalDateTime.now());

        Bid savedBid = bidRepository.save(bid);

        auction.setCurrentBid(savedBid.getBidAmount());
        auctionRepository.save(auction);

        return bidMapper.toDto(savedBid);
    }

    @Override
    @Transactional
    public void deleteBid(Integer id) {
        if (!bidRepository.existsById(id)) {
            throw new EntityNotFoundException("Bid not found with ID: " + id);
        }
        bidRepository.deleteById(id);
    }
}
