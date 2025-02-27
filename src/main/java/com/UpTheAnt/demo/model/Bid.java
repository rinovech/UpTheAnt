package com.UpTheAnt.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "bids") 
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    // Геттеры и сеттеры

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    @Override
    public String toString() {
    return "Bid{" +
            "bidId=" + bidId +
            ", bidAmount=" + bidAmount +
            ", bidTime=" + bidTime +
            ", user=" + (user != null ? user.getUserId() : "null") +
            ", auction=" + (auction != null ? auction.getAuctionId() : "null") +
            '}';
    }
}