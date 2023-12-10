package com.example.board.controller;

import com.example.board.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("api/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String newPassword) {

        boolean isPasswordChanged = accountService.changePassword(username, newPassword);

        if (isPasswordChanged) {
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("비밀번호 변경에 실패하였습니다.");
        }
    }

    // 닉네임 변경 요청 처리
    @PostMapping("api/change-nickname")
    public ResponseEntity<String> changeNickname(@RequestParam String username, @RequestParam String newNickname) {
        boolean isChanged = accountService.changeNickname(username, newNickname);

        if (isChanged) {
            return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("닉네임 변경에 실패했습니다.");
        }
    }


}
