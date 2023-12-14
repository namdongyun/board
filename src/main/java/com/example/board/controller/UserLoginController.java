package com.example.board.controller;

import com.example.board.dto.AccountDTO;
import com.example.board.dto.LoginRequest;
import com.example.board.dto.TokenDTO;
import com.example.board.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserLoginController {

    private final UserLoginService userLoginService;

    // JWT 이용한 로그인 컨트롤러
    @PostMapping("/api/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok().body(userLoginService.login(loginRequest));
    }

    // JWT가 만료되었을 때 refreshToken을 받아 새로운 accessToken 발급
    @PostMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody Map<String, String> payload) throws Exception {
        return ResponseEntity.ok().body(userLoginService.refreshToken(payload));
    }

    // 회원가입 컨트롤러
    @PostMapping("/api/register")
    public void registerUserAccount(@RequestBody AccountDTO accountDto) {

        userLoginService.save(accountDto);
    }

    // 로그아웃 컨트롤러
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(){
        userLoginService.logout();
        return ResponseEntity.ok().build();
    }
}