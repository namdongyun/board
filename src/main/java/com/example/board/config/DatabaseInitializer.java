package com.example.board.config;

import com.example.board.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// 서버 실행시 refreshToken DB 초기화
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void run(String... args) throws Exception {
        refreshTokenRepository.deleteAll();
    }
}
