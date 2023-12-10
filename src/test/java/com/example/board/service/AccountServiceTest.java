package com.example.board.service;

import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class  AccountServiceTest {

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
        account = new Account();
        account.setUsername("user");
        account.setPassword("encodedPassword");
    }

    @Test
    void 비밀번호_확인_메소드_true반환_확인() {
        // Arrange
        when(accountRepository.findByUsername("user")).thenReturn(Optional.of(account));
        when(bCryptPasswordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        // Act
        boolean result = accountService.checkPassword("user", "rawPassword");

        // Assert
        assertTrue(result);
    }

    @Test
    void 비밀번호_확인_메소드_false반환_확인() {
        // Arrange
        when(accountRepository.findByUsername("user")).thenReturn(Optional.of(account));
        when(bCryptPasswordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act
        boolean result = accountService.checkPassword("user", "wrongPassword");

        // Assert
        assertFalse(result);
    }

    @Test
    void 비밀번호_변경_테스트_true값_확인() {
        // Arrange
        when(accountRepository.findByUsername("user")).thenReturn(Optional.of(account));
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        boolean result = accountService.changePassword("user", "newPassword");

        // Assert
        assertTrue(result);
        // accountRepository.save 메소드가 실제로 account 객체와 함께 호출되었는지를 검증합니다.
        verify(accountRepository).save(account);
        // bCryptPasswordEncoder.encode 메소드가 "newPassword" 인자와 함께 호출되었는지 검증합니다.
        verify(bCryptPasswordEncoder).encode("newPassword");
    }

    @Test
    void 비밀번호_변경_테스트_account를_찾을_수_없을_때_false값_확인() {
        // Arrange
        when(accountRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        // Act
        boolean result = accountService.changePassword("nonExistingUser", "newPassword");

        // Assert
        assertFalse(result);
    }

    @Test
    void 닉네임_변경_테스트_true일_때() {
        // given
        account.setNickname("oldNickname");

        String username = "user";
        String newNickname = "newNickname";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        // when
        boolean result = accountService.changeNickname(username, newNickname);

        // then
        assertTrue(result);
        verify(accountRepository).save(account);
        assertEquals(newNickname, account.getNickname());
    }

    @Test
    void 닉네임_변경_테스트_해당_account를_찾을_수_없을_때() {
        // given
        String username = "nonExistingUser";
        String newNickname = "newNickname";

        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        boolean result = accountService.changeNickname(username, newNickname);

        // then
        assertFalse(result);
        // accountRepository의 save 메소드가 Account 클래스의 인스턴스와 함께 단 한 번도 호출되지 않았음을 확인
        verify(accountRepository, never()).save(any(Account.class));
    }
}
