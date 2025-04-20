package com.uptheant.demo.service.user;

import com.uptheant.demo.dto.auction.AuctionParticipationDTO;
import com.uptheant.demo.dto.user.UserCreateDTO;
import com.uptheant.demo.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Integer id);
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByUsername(String username);
    void deleteUser(Integer id);
    List<AuctionParticipationDTO> getUserAuctionParticipations(String usernamed);
    List<AuctionParticipationDTO> getUserAuctionCreations(String usernamed);
}