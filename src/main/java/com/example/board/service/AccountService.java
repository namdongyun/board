package com.example.board.service;

import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 비밀번호 확인 메소드
    public boolean checkPassword(String username, String currentPassword) {
        // 사용자 ID로 계정 정보를 찾습니다.
        Account account = accountRepository.findByUsername(username).orElse(null);

        return account != null && bCryptPasswordEncoder.matches(currentPassword, account.getPassword()); // True or False
    }

    // 비밀번호 변경 메소드
    public boolean changePassword(String username, String newPassword) {
        // 사용자 ID로 계정 정보를 찾습니다.
        Account account = accountRepository.findByUsername(username).orElse(null);

        if (account != null) {
            // 새로운 비밀번호를 암호화합니다.
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

            // 암호화된 비밀번호로 업데이트합니다.
            account.setPassword(encodedPassword);

            // 변경된 정보를 저장합니다.
            accountRepository.save(account);

            return true; // 비밀번호 변경 성공
        }

        return false; // 사용자를 찾을 수 없거나 비밀번호 변경 실패
    }

    // 닉네임 변경 메소드
    public boolean changeNickname(String username, String newNickname) {
        // 사용자 ID로 계정 정보를 찾습니다.
        Account account = accountRepository.findByUsername(username).orElse(null);

        if (account != null) {
            // 새로운 닉네임으로 업데이트합니다.
            account.setNickname(newNickname);

            // 변경된 정보를 저장합니다.
            accountRepository.save(account);

            return true; // 닉네임 변경 성공
        }

        return false; // 사용자를 찾을 수 없거나 닉네임 변경 실패
    }

}
