package com.example.board.repository;

import com.example.board.entity.Account;
import com.example.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // refreshToken이 존재하는지 확인하는 쿼리 메서드
    Boolean existsByRfToken(String rfToken);
    void deleteByAccount(Account account);
}
