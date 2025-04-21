package com.uptheant.demo.service.bid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.dto.user.UserBidDTO;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.auction.AuctionService;
import com.uptheant.demo.service.mapper.BidMapper;
import com.uptheant.demo.service.user.UserService;
import com.uptheant.demo.service.validation.BidValidator;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidValidator bidValidator;
    private final BidMapper bidMapper;
    private final AuctionService auctionService;
    private final UserService userService;

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

    public List<UserBidDTO> getUserBidsForAuction(Integer userId, Integer auctionId) {

        userService.getUserById(userId); 
        auctionService.getAuctionById(auctionId);
        
        return bidRepository.findByUserUserIdAndAuctionAuctionId(userId, auctionId)
                .stream()
                .map(this::convertToUserBidDTO)
                .collect(Collectors.toList());
    }
    
    private UserBidDTO convertToUserBidDTO(Bid bid) {
        return UserBidDTO.builder()
                .amount(bid.getBidAmount())
                .bidTime(bid.getBidTime())
                .build();
    }

    public BidResponseDTO placeBidByUsername(BidCreateDTO bidCreateDTO, String username, Integer auctionId) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        
        Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new EntityNotFoundException("Аукцион не найден"));
        
        bidValidator.validateBidCreation(bidCreateDTO, user.getUserId(), auctionId);
        
        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setBidAmount(bidCreateDTO.getBidAmount());
        bid.setBidTime(LocalDateTime.now());
        
        Bid savedBid = bidRepository.save(bid);

        auction.setCurrentBid(savedBid.getBidAmount());
        auctionRepository.save(auction);
        
        return bidMapper.toDto(savedBid);
    }
}
