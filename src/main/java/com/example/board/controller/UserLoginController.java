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

    @Autowired
    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/api/register")
    public void registerUserAccount(@RequestBody AccountDTO accountDto) {

        userService.save(accountDto);
    }
}
