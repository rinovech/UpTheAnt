package com.uptheant.demo.service.auction;

import com.uptheant.demo.model.User;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionParticipationDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.dto.user.UserBidDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.AuctionMapper;
import com.uptheant.demo.service.validation.AuctionValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private static final Logger logger = LoggerFactory.getLogger(AuctionServiceImpl.class);

    private final AuctionRepository auctionRepository;
    private final AuctionValidator auctionValidator;
    private final AuctionMapper auctionMapper;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    @Override
    public List<AuctionResponseDTO> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(auctionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionResponseDTO getAuctionById(Integer id) {
        return auctionRepository.findById(id)
                .map(auctionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found"));
    }

    @Override
    public AuctionResponseDTO createAuction(AuctionCreateDTO dto, Integer sellerId) {

        auctionValidator.validateCreation(dto);

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        Auction auction = new Auction();
        auction.setName(dto.getName());
        auction.setDescription(dto.getDescription());
        auction.setStartPrice(dto.getStartPrice());
        auction.setMinBidStep(dto.getMinBidStep());
        auction.setStartTime(dto.getStartTime());
        auction.setEndTime(dto.getEndTime());

        auction.setStatus(false);
        auction.setUser(seller);

        Auction savedAuction = auctionRepository.save(auction);
        
        logger.info("Auction created: ID {}, Name: {}", savedAuction.getAuctionId(), savedAuction.getName());
        
        return auctionMapper.toDto(savedAuction);
    }

    @Override
    @Transactional
    public void closeAuction(Integer auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new EntityNotFoundException("Auction not found"));

        auctionValidator.validateClosing(auction);
    
        if (auction.getEndTime() == null) {
            throw new BusinessRuleException("Auction end time is not set");
        }
    
        LocalDateTime now = LocalDateTime.now();
        if (auction.getEndTime().isAfter(now)) {
            logger.info("Auction end time not reached: ID {}", auctionId);
            return;
        }
    
        if (!auction.isStatus()) {
            logger.info("Auction already closed: ID {}", auctionId);
            return;
        }
    
        auction.setStatus(false);
        auctionRepository.save(auction);
        
        logger.info("Auction {} closed {}", auction.getAuctionId(), 
            auction.getCurrentBid() != null ? "with winner" : "without bids");
    }

    @Transactional
    @Override
    public void deleteAuction(Integer id) {

        if(!auctionRepository.existsById(id)) {
            throw new EntityNotFoundException("Auction with ID " + id + " not found");
        }

        Auction auction = auctionRepository.findById(id).orElse(null);

        if (auction != null && auction.getCurrentBid() != null) {
            throw new BusinessRuleException("Cannot delete auction with existing bids");
        }

        auctionRepository.deleteById(id);
        
        logger.info("Auction deleted: ID {}", id);
    }

    public AuctionParticipationDTO getAuctionWithBids(Integer id) {
        Auction auction = auctionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Аукцион не найден"));

        List<UserBidDTO> bids = bidRepository.findByAuction(auction).stream()
            .map(this::convertToUserBidDTO)
            .collect(Collectors.toList());

        return AuctionParticipationDTO.builder()
            .auctionId(auction.getAuctionId())
            .name(auction.getName())
            .description(auction.getDescription())
            .startPrice(auction.getStartPrice())
            .currentBid(auction.getCurrentBid())
            .endTime(auction.getEndTime())
            .minBidStep((auction.getMinBidStep()))
            .userBids(bids)
            .sellerUsername(auction.getUser().getUsername())
            .build();
    }

    private UserBidDTO convertToUserBidDTO(Bid bid) {
        return UserBidDTO.builder()
            .bidderUsername(bid.getUser().getUsername())
            .amount(bid.getBidAmount())
            .bidTime(bid.getBidTime())
            .build();
    }
}