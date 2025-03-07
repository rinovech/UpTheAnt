package com.uptheant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptheant.demo.model.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
