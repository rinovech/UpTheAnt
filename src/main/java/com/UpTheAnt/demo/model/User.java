package com.uptheant.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users", indexes = { 
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_username", columnList = "username", unique = true) 
})
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString 
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer userId;

    @NotNull 
    private String name;

    @NotNull 
    private String username;

    @NotNull 
    private String email;

    @NotNull 
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Auction> auctions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Bid> bids;

    public User(String name, String username, String email, String password, UserRole role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}