package com.uptheant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptheant.demo.model.Bid;

public interface BidRepository extends JpaRepository<Bid, Integer> {
}