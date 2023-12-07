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

    // 회원가입
    public void save(AccountDTO accountDto) {
        Account account = new Account();
        account.setUsername(accountDto.getUsername());
        account.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
        account.setEmail(accountDto.getEmail());
        account.setNickname(accountDto.getNickname());
        account.setRole("USER");

        accountRepository.save(account);
    }
}
