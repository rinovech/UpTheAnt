package com.UpTheAnt.demo.repository;

import com.UpTheAnt.demo.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
