package com.uptheant.demo.service.bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uptheant.demo.dto.bid.BidCreateDTO;
import com.uptheant.demo.dto.bid.BidResponseDTO;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BidResponseDTO> getAllBids() {
        return bidRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BidResponseDTO getBidById(Integer id) {
        return bidRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
    }

    @Override
    public BidResponseDTO createBid(BidCreateDTO bidCreateDTO, Integer userId, Integer auctionId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        Bid bid = new Bid();
        bid.setBidAmount(bidCreateDTO.getBidAmount());
        bid.setBidTime(LocalDateTime.now());
        bid.setAuction(auction);
        bid.setUser(user);

        Bid savedBid = bidRepository.save(bid);
        return convertToResponseDTO(savedBid);
    }

    @Override
    public void deleteBid(Integer id) {
        bidRepository.deleteById(id);
    }

    private BidResponseDTO convertToResponseDTO(Bid bid) {
        BidResponseDTO bidResponseDTO = new BidResponseDTO();
        bidResponseDTO.setBidAmount(bid.getBidAmount());
        bidResponseDTO.setBidTime(bid.getBidTime());
        bidResponseDTO.setUserId(bid.getUser().getUserId());
        bidResponseDTO.setAuctionId(bid.getAuction().getAuctionId());

        if (bid.getUser() != null) {
            bidResponseDTO.setUserId(bid.getUser().getUserId());
        } else {
            bidResponseDTO.setUserId(null);
        }

        if (bid.getAuction() != null) {
            bidResponseDTO.setAuctionId(bid.getAuction().getAuctionId());
        } else {
            bidResponseDTO.setAuctionId(null);        
        }

        return bidResponseDTO;
    }
    
}
