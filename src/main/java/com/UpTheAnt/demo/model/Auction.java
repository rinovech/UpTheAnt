package com.uptheant.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auctions") 
@Getter
@Setter
@ToString
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

}