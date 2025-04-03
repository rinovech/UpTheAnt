package com.uptheant.demo.service.auction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uptheant.demo.model.User;
import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.exception.BusinessRuleException;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.AuctionMapper;
import com.uptheant.demo.service.validation.AuctionValidator;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionValidator auctionValidator;
    private final AuctionMapper auctionMapper;
    private final UserRepository userRepository;

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
        return auctionMapper.toDto(savedAuction);
    }

    @Override
    public void closeAuction(Integer auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new EntityNotFoundException("Auction not found"));
        
        auctionValidator.validateClosing(auction);
        
        auction.setStatus(true);
        auctionRepository.save(auction);
    }

    @Transactional
    @Override
    public void deleteAuction(Integer id) {
        if (!auctionRepository.existsById(id)) {
            throw new EntityNotFoundException("Auction with ID " + id + " not found");
        }

        Auction auction = auctionRepository.findById(id).orElse(null);
        
        if (auction != null) {
            if (auction.getCurrentBid() != null) {
                throw new BusinessRuleException("Cannot delete auction with existing bids");
            }
        }

        auctionRepository.deleteById(id);
    }

}