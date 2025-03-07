package com.uptheant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uptheant.demo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
