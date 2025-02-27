package com.UpTheAnt.demo.repository;

import com.UpTheAnt.demo.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Integer> {
}