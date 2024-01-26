package com.example.board.controller;

import com.example.board.dto.AccountDetailDTO;
import com.example.board.service.AccountDetailService;
import com.example.board.service.TitleEnhancementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/titleEnhancement")
public class TitleEnhancementController {

    private final TitleEnhancementService titleEnhancementService;

    @PostMapping("/update")
    public ResponseEntity<?> updateAccountDetail(@RequestBody AccountDetailDTO accountDetailDTO) {
        boolean success = titleEnhancementService.updateTitleAndPoints(accountDetailDTO);

        if (success) {
            return ResponseEntity.ok().build(); // 성공 응답
        } else {
            return ResponseEntity.badRequest().body("계정 정보를 업데이트할 수 없습니다."); // 실패 응답
        }
    }

}
