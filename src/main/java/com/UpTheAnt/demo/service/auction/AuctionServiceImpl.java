package com.uptheant.demo.service.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptheant.demo.dto.auction.AuctionCreateDTO;
import com.uptheant.demo.dto.auction.AuctionResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AuctionResponseDTO> getAllAuctions() {
        return auctionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionResponseDTO getAuctionById(Integer id) {
        return auctionRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    @Override
    public AuctionResponseDTO createAuction(AuctionCreateDTO auctionCreateDTO, Integer sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Auction auction = new Auction();
        auction.setName(auctionCreateDTO.getName());
        auction.setDescription(auctionCreateDTO.getDescription());
        auction.setStartPrice(auctionCreateDTO.getStartPrice());
        auction.setMinBidStep(auctionCreateDTO.getMinBidStep());
        auction.setStartTime(auctionCreateDTO.getStartTime());
        auction.setEndTime(auctionCreateDTO.getEndTime());
        auction.setCurrentBid(null);
        auction.setStatus(false);
        auction.setUser(seller);

        Auction savedAuction = auctionRepository.save(auction);
        return convertToResponseDTO(savedAuction);
    }

    public void deleteAuction(Integer id) {
        auctionRepository.deleteById(id);
    }

    private AuctionResponseDTO convertToResponseDTO(Auction auction) {
        AuctionResponseDTO auctionResponseDTO = new AuctionResponseDTO();
        auctionResponseDTO.setName(auction.getName());
        auctionResponseDTO.setDescription(auction.getDescription());
        auctionResponseDTO.setStartPrice(auction.getStartPrice());
        auctionResponseDTO.setCurrentBid(auction.getCurrentBid());
        auctionResponseDTO.setMinBidStep(auction.getMinBidStep());
        auctionResponseDTO.setStartTime(auction.getStartTime());
        auctionResponseDTO.setEndTime(auction.getEndTime());
        auctionResponseDTO.setSellerId(auction.getUser().getUserId());

        if (auction.getUser() != null) {
            auctionResponseDTO.setSellerId(auction.getUser().getUserId());
        } else {
            auctionResponseDTO.setSellerId(null);
        }

        return auctionResponseDTO;
    }
}