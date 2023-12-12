package com.example.board.controller;

import com.example.board.dto.AccountDTO;
import com.example.board.dto.LoginRequest;
import com.example.board.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserLoginController {

    private final UserLoginService userLoginService;

    // JWT 이용한 로그인 컨트롤러
    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok().body(userLoginService.login(loginRequest));
    }

    // 회원가입 컨트롤러
    @PostMapping("/api/register")
    public void registerUserAccount(@RequestBody AccountDTO accountDto) {

        userLoginService.save(accountDto);
    }
}