package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_username", referencedColumnName = "id")
    private Account account;

    @Column(nullable = false, unique = true)
    private String rfToken;

    @Column(nullable = false)
    private Instant expiryDate;

    // getters and setters 생략...
}