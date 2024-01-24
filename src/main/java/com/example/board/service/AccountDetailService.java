package com.example.board.service;

import com.example.board.entity.Account;
import com.example.board.entity.AccountDetail;
import com.example.board.repository.AccountDetailRepository;
import com.example.board.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountDetailService {
    private final AccountDetailRepository accountDetailRepository;

    // 현재 계정 money 조회
    public Long loadMoney() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

        return accountDetailRepository.findById(account.getId())
                .map(AccountDetail::getMoney)
                .orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));
    }

    // 현재 계정 detail 조회
    public Long loadDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

    }
}
