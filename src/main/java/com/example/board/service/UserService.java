package com.example.board.service;

import com.example.board.dto.AccountDTO;
import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(AccountDTO accountDto) {
        Account account = new Account();
        account.setUsername(accountDto.getUsername());
        account.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
        account.setEmail(accountDto.getEmail());
        account.setRole("USER");

        accountRepository.save(account);
    }

    public AccountDTO login(AccountDTO accountDTO) {
        Optional<Account> byUsername = accountRepository.findByUsername(accountDTO.getUsername());
        if (byUsername.isPresent()) {
            // 조회 결과 있음
            Account account = byUsername.get();
            if (account.getPassword().equals(accountDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환
                return AccountDTO.toUserDTO(account);
            } else {
                return null;
            }
        } else {
            // 조회 결과 없음
            return null;
        }
    }
}
