package com.uptheant.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    List<Auction> findByStatusFalseAndStartTimeBefore(LocalDateTime currentTime);

    List<Auction> findByStatusTrueAndEndTimeBefore(LocalDateTime currentTime);

    List<Auction> findByUser(User user);
}
