package com.uptheant.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.User;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    List<Auction> findByStatusFalseAndStartTimeBefore(LocalDateTime currentTime);

    List<Auction> findByStatusTrueAndEndTimeBefore(LocalDateTime currentTime);

    List<Auction> findByUser(User user);

    List<Auction> findByStartPrice(BigDecimal startPrice);

    List<Auction> findByUserOrderByStartTimeDesc(User user);
    List<Auction> findByUserOrderByEndTimeDesc(User user);

    @Query("SELECT a FROM Auction a JOIN a.bids b " +
           "WHERE a.currentBid = b.bidAmount " +
           "AND b.user.userId = :userId AND a.status = FALSE" )
    List<Auction> findWinningAuctionsByUser(@Param("userId") Integer userId);

}
