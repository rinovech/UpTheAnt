package com.uptheant.demo.service.user;

import com.uptheant.demo.dto.auction.AuctionParticipationDTO;
import com.uptheant.demo.dto.user.UserActivityDTO;
import com.uptheant.demo.dto.user.UserBidDTO;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;
import com.uptheant.demo.dto.user.UserUpdateDTO;
import com.uptheant.demo.exception.EntityNotFoundException;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;
import com.uptheant.demo.model.UserRole;
import com.uptheant.demo.repository.AuctionRepository;
import com.uptheant.demo.repository.BidRepository;
import com.uptheant.demo.repository.UserRepository;
import com.uptheant.demo.service.mapper.UserMapper;
import com.uptheant.demo.service.validation.UserValidator;

import lombok.RequiredArgsConstructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator; 
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        userValidator.validateCreation(userCreateDTO);
        User user = new User();
        user.setName(userCreateDTO.getName().trim());
        user.setUsername(userCreateDTO.getUsername().trim());
        user.setEmail(userCreateDTO.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        return userRepository.findUserByEmail(email.trim().toLowerCase())
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username.trim())
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    private UserResponseDTO convertToDto(User user) {
        return UserResponseDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository; 

    @Override
    @Transactional(readOnly = true)
    public List<AuctionParticipationDTO> getUserAuctionParticipations(String username) {


        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        Integer userId = user.getUserId();

        List<Auction> auctions = bidRepository.findDistinctAuctionsByUserId(userId);

        return auctions.stream()
                .map(auction -> {
                    List<UserBidDTO> userBids = bidRepository
                            .findUserBidsForAuction(userId, auction.getAuctionId())
                            .stream()
                            .map(this::convertToUserBidDTO)
                            .toList();

                    return buildAuctionParticipationDTO(auction, userBids);
                })
                .toList();
    }

    public UserBidDTO convertToUserBidDTO(Bid bid) {
        return UserBidDTO.builder()
                .bidderUsername(bid.getUser().getUsername())
                .amount(bid.getBidAmount())
                .bidTime(bid.getBidTime())
                .build();
    }

    private AuctionParticipationDTO buildAuctionParticipationDTO(Auction auction, List<UserBidDTO> userBids) {
        return AuctionParticipationDTO.builder()
                .auctionId(auction.getAuctionId())
                .name(auction.getName())
                .description(auction.getDescription())
                .startPrice(auction.getStartPrice())
                .currentBid(auction.getCurrentBid())
                .minBidStep(auction.getMinBidStep())
                .startTime(auction.getStartTime())
                .sellerUsername(auction.getUser().getUsername())
                .endTime(auction.getEndTime())
                .userBids(userBids)
                .build();
    }

    public List<AuctionParticipationDTO> getUserAuctionCreations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        List<Auction> userAuctions = auctionRepository.findByUser(user);

        return userAuctions.stream()
                .map(this::convertToAuctionParticipationDTO)
                .collect(Collectors.toList());
    }

    private AuctionParticipationDTO convertToAuctionParticipationDTO(Auction auction) {
        return AuctionParticipationDTO.builder()
                .auctionId(auction.getAuctionId())
                .name(auction.getName())
                .description(auction.getDescription())
                .startPrice(auction.getStartPrice())
                .currentBid(auction.getCurrentBid()) 
                .minBidStep(auction.getMinBidStep())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .sellerUsername(auction.getUser().getUsername())
                .userBids(getUserBidsForAuction(auction))
                .build();
    }

    private List<UserBidDTO> getUserBidsForAuction(Auction auction) {
        return bidRepository.findByAuction(auction).stream()
                .map(this::convertToUserBidDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserActivityDTO> getUserActivities(String username, int limit) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<UserActivityDTO> activities = new ArrayList<>();

        List<Auction> createdAuctions = auctionRepository.findByUserOrderByStartTimeDesc(user);
        createdAuctions.stream()
                .map(auction -> UserActivityDTO.builder()
                        .type(UserActivityDTO.ActivityType.AUCTION_CREATED)
                        .auctionName(auction.getName())
                        .timestamp(auction.getStartTime())
                        .build())
                .forEach(activities::add);

        List<Bid> userBids = bidRepository.findByUserOrderByBidTimeDesc(user);
        userBids.stream()
                .map(bid -> UserActivityDTO.builder()
                        .type(UserActivityDTO.ActivityType.BID)
                        .auctionName(bid.getAuction().getName())
                        .amount(bid.getBidAmount())
                        .timestamp(bid.getBidTime())
                        .build())
                .forEach(activities::add);

        List<Auction> wonAuctions = auctionRepository.findWinningAuctionsByUser(user.getUserId());
        wonAuctions.stream()
                .map(auction -> UserActivityDTO.builder()
                        .type(UserActivityDTO.ActivityType.AUCTION_WON)
                        .auctionName(auction.getName())
                        .amount(auction.getCurrentBid())
                        .timestamp(auction.getEndTime())
                        .build())
                .forEach(activities::add);
        
        List<Auction> finishedAuctions = auctionRepository.findByUserOrderByEndTimeDesc(user);
        finishedAuctions.stream()
                .map(auction -> UserActivityDTO.builder()
                        .type(UserActivityDTO.ActivityType.AUCTION_FINISHED)
                        .auctionName(auction.getName())
                        .timestamp(auction.getEndTime())
                        .build())
                .forEach(activities::add);

        return activities.stream()
                .sorted(Comparator.comparing(UserActivityDTO::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportUserBidsToCsv(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        List<Bid> bids = bidRepository.findByUserUserId(user.getUserId());
        
        try (
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = CSVFormat.DEFAULT.builder()
                .setHeader("Auction", "Bid Amount", "Bid Time", "Status", "End Time")
                .setDelimiter(';')
                .build()
                .print(writer)
        ) {

            outputStream.write(0xEF);
            outputStream.write(0xBB);
            outputStream.write(0xBF);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            
            for (Bid bid : bids) {
                Auction auction = bid.getAuction();
                String status = auction.getEndTime().isAfter(LocalDateTime.now()) ? "Active" : "Finished";
                
                csvPrinter.printRecord(
                    auction.getName(), 
                    String.format(Locale.US, "%.2f ₽", bid.getBidAmount()), 
                    bid.getBidTime().format(formatter),
                    status,
                    auction.getEndTime().format(formatter)
                );
            }
            
            csvPrinter.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV", e);
        }
    }
}



