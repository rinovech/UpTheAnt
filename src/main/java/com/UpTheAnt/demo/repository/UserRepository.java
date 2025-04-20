package com.uptheant.demo.repository;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;
import com.uptheant.demo.model.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findByRole(UserRole role);

    @Query("SELECT DISTINCT a FROM Auction a " +
    "JOIN a.bids b " +
    "WHERE b.user.userId = :userId")
    List<Auction> findAuctionsByUserId(@Param("userId") Integer userId);

}