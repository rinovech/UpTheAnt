package com.UpTheAnt.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "auctions") 
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auctionId;
    
    private String name;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal minBidStep;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal currentBid;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Геттеры и сеттеры

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
   
    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getMinBidStep() {
        return minBidStep;
    }

    public void setMinBidStep(BigDecimal minBidStep) {
        this.minBidStep = minBidStep;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(BigDecimal currentBid) {
        this.currentBid = currentBid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
    return "Auction{" +
            "auctionId=" + auctionId +
            ", name=" + name +
            ", descption=" + description +
            ", startPrice=" + startPrice +
            ", minBidStep=" + minBidStep +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", currentBid=" + currentBid +
            ", ststus=" + status +
            ", user=" + (user != null ? user.getUserId() : "null") +
            '}';
    }
}