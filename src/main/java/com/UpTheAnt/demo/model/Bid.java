package com.UpTheAnt.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {

    private Integer bidId;
    private Integer userId; // Идентификатор пользователя, сделавшего ставку
    private Integer auctionId; // Идентификатор аукциона
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;

    // Геттеры и сеттеры
    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
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
}