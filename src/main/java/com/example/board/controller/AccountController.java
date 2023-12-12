package com.example.board.controller;

import com.example.board.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;


    // 비밀번호 변경 요청 처리
    @PostMapping("api/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String newPassword) {

        boolean isPasswordChanged = accountService.changePassword(newPassword);

        if (isPasswordChanged) {
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("비밀번호 변경에 실패하였습니다.");
        }
    }

    // 닉네임 변경 요청 처리
    @PostMapping("api/change-nickname")
    public ResponseEntity<String> changeNickname(@RequestParam String newNickname) {
        boolean isChanged = accountService.changeNickname(newNickname);

        if (isChanged) {
            return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("닉네임 변경에 실패했습니다.");
        }
    }


}
