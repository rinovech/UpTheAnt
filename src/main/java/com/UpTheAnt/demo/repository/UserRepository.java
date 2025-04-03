package com.uptheant.demo.repository;

import com.uptheant.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}