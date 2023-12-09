package com.example.board.controller;

import com.example.board.dto.AccountDTO;
import com.example.board.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserLoginController {

    private final UserLoginService userLoginService;

    @Autowired
    public UserLoginController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    // 회원가입
    @PostMapping("/api/register")
    public void registerUserAccount(@RequestBody AccountDTO accountDto) {

        userLoginService.save(accountDto);
    }
}
