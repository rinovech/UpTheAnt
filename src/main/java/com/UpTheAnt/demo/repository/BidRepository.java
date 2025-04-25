package com.uptheant.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uptheant.demo.model.Auction;
import com.uptheant.demo.model.Bid;
import com.uptheant.demo.model.User;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Query("SELECT DISTINCT a FROM Auction a JOIN a.bids b WHERE b.user.userId = :userId")
    List<Auction> findDistinctAuctionsByUserId(@Param("userId") Integer userId);
    

    @Query("SELECT b FROM Bid b WHERE b.user.userId = :userId AND b.auction.auctionId = :auctionId ORDER BY b.bidTime DESC")
    List<Bid> findUserBidsForAuction(@Param("userId") Integer userId, @Param("auctionId") Integer auctionId);

    List<Bid> findByUserUserIdAndAuctionAuctionId(Integer userId, Integer auctionId);

    List<Bid> findByAuction(Auction auction);

    List<Bid> findByUserOrderByBidTimeDesc(User user);
}