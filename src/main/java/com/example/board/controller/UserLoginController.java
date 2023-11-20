package com.example.board.controller;

import com.example.board.dto.AccountDTO;
import com.example.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserLoginController {

    private final UserService userService;

    @Autowired
    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    // 홈페이지 이동
    @GetMapping("/")
    public String home() {
        return "Login/index"; // 로그인 페이지 경로 반환
    }

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage() {
        return "Login/login"; // 로그인 페이지 경로 반환
    }

    // 로그아웃 기능
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return "redirect:/Login/login"; // 로그인 페이지로 리다이렉트
    }

    // 회원가입 페이지 이동
    @GetMapping("/register")
    public String register() {

        return "Login/register"; // 로그인 페이지 경로 반환
    }

    // 회원가입 완료 버튼 클릭 시
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("userDTO") AccountDTO accountDto) {

        userService.save(accountDto);
        return "redirect:/";
    }
}
