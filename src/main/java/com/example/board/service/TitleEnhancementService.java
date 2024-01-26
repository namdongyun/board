package com.example.board.service;

import com.example.board.dto.AccountDetailDTO;
import com.example.board.entity.Account;
import com.example.board.repository.AccountDetailRepository;
import com.example.board.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TitleEnhancementService {

    private final AccountDetailRepository accountDetailRepository;

    @Transactional
    public boolean updateTitleAndPoints(AccountDetailDTO accountDetailDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

        return accountDetailRepository.findById(account.getId())
                .map(accountDetail -> {
                    // accountDetailDTO에서 받은 값으로 accountDetail 업데이트
                    accountDetail.setTitleLevel(accountDetailDTO.getTitleLevel());
                    accountDetail.setMoney(accountDetailDTO.getMoney());
                    accountDetailRepository.save(accountDetail);
                    return true; // 성공적으로 업데이트 되었을 때 true 반환
                }).orElse(false); // 찾지 못했을 때 false 반환
    }

}
