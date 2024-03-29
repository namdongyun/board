package com.example.board.controller;

import com.example.board.service.AccountDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accountDetail")
public class AccountDetailController {

    private final AccountDetailService accountDetailService;

    // 현재 계정 money 조회
    @GetMapping("/loadMoney")
    public ResponseEntity<Long> loadMoney() {
        return ResponseEntity.ok(accountDetailService.loadMoney());
    }
}
