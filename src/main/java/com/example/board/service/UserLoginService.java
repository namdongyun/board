package com.example.board.service;

import com.example.board.dto.AccountDTO;
import com.example.board.dto.LoginRequest;
import com.example.board.dto.TokenDTO;
import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.RefreshTokenRepository;
import com.example.board.security.PrincipalDetails;
import com.example.board.security.PrincipalDetailsService;
import com.example.board.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserLoginService {
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    // JWT 이용한 로그인 서비스
    public TokenDTO login(LoginRequest loginRequest) throws Exception {
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

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(jwtUtil.createJwt(loginRequest.getUsername()));
        tokenDTO.setRefreshToken(jwtUtil.createRefreshToken(loginRequest.getUsername()));

        return tokenDTO;
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

    // 로그아웃
    @Transactional
    public void logout() {
        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account currentAccount = principalDetails.getAccount();

        refreshTokenRepository.deleteByAccount(currentAccount);
    }

    // refreshToken을 이용해 accessToken 재발급
    public String refreshToken(Map<String, String> payload) {

        String refreshToken = payload.get("refreshToken");

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            // refreshToken이 유효하지 않다면 에러를 반환합니다.
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
        // refreshToken이 유효하다면 새로운 accessToken을 생성합니다.
        return jwtUtil.createJwt(jwtUtil.getUsername(refreshToken));
    }
}
