package com.example.board.service;

import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import com.example.board.security.PrincipalDetails;
import com.example.board.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 어노테이션을 초기화합니다.
        
        // account 객체 생성
        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setEmail("1@1");
        account.setNickname("nickname");
        account.setPassword("encodedPassword");
        account.setRole("USER");

        // 현재 인증된 사용자 설정
        PrincipalDetails principalDetails = new PrincipalDetails(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        // SecurityContext에 Authentication 객체를 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void 비밀번호_변경_테스트_() {
        // Arrange
        String newPassword = "newPassword";

        // Act
        boolean result = accountService.changePassword(newPassword);

        // Assert
        assertTrue(result);
        // accountRepository.save 메소드가 실제로 account 객체와 함께 호출되었는지를 검증합니다.
        verify(accountRepository).save(any(Account.class)); // 저장 메소드가 호출되었는지 확인
        verify(bCryptPasswordEncoder).encode("newPassword"); // 새 비밀번호가 인코딩되었는지 확인
        assertEquals("encodedPassword", account.getPassword()); // 실제 비밀번호가 인코딩된 비밀번호로 설정되었는지 확인
    }


    @Test
    void 닉네임_변경_테스트() {
        // given
        String newNickname = "newNickname";

        // when
        boolean result = accountService.changeNickname(newNickname);

        // then
        assertTrue(result);
        verify(accountRepository).save(account);
        assertEquals(newNickname, account.getNickname());
    }
}
