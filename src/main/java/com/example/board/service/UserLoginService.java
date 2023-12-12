package com.example.board.service;

import com.example.board.dto.AccountDTO;
import com.example.board.dto.LoginRequest;
import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import com.example.board.security.PrincipalDetailsService;
import com.example.board.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLoginService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    // JWT 이용한 로그인 서비스
    public String login(LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("잘못된 사용자 이름 또는 비밀번호입니다.", e);
        }

        return jwtUtil.createJwt(
                loginRequest.getUsername()
        );
    }

    // 회원가입 서비스
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
