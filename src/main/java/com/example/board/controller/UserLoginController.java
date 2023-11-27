package com.example.board.controller;

import com.example.board.dto.AccountDTO;
import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import com.example.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserLoginController {

    private final UserService userService;
    private final AccountRepository accountRepository;

    @Autowired
    public UserLoginController(UserService userService, AccountRepository accountRepository) {
        this.userService = userService;
        this.accountRepository = accountRepository;
    }

//    // 홈페이지 이동
//    @GetMapping("/")
//    public String home() {
//        return "Login/index"; // 로그인 페이지 경로 반환
//    }

//    // 로그인 페이지 이동
//    @GetMapping("/login")
//    public String loginPage() {
//        return "Login/login"; // 로그인 페이지 경로 반환
//    }

//    // 로그아웃 기능
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate(); // 세션 무효화
//        }
//        return "redirect:/Login/login"; // 로그인 페이지로 리다이렉트
//    }

//    // 회원가입 페이지 이동
//    @GetMapping("/register")
//    public String register() {
//
//        return "Login/register"; // 로그인 페이지 경로 반환
//    }

    // 회원가입 완료 버튼 클릭 시
    @PostMapping("/api/register")
    public void registerUserAccount(@RequestBody AccountDTO accountDto) {

        userService.save(accountDto);
    }
    
    // account Role 정보 반환
//    @GetMapping("/account/profile")
//    // Authentication 객체에 클라이언트가 서버로 보내는 자격 증명 정보를 받습니다.
//    public ResponseEntity<Map<String, Object>> getUserProfile(Authentication authentication) {
//        String username = authentication.getName();
//        Account account = accountRepository.findByUsername(username).orElseThrow(() ->
//                new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("username", account.getUsername());
//        response.put("role", account.getRole());
//
//        return ResponseEntity.ok(response);
//    }
}
