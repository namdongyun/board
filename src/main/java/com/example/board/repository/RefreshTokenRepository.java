package com.example.board.repository;

import com.example.board.entity.Account;
import com.example.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRfToken(String rfToken);
    void deleteByAccount(Account account);
}
