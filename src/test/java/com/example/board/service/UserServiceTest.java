package com.example.board.service;

import com.example.board.dto.AccountDTO;
import com.example.board.entity.Account;
import com.example.board.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 어노테이션을 초기화합니다.
    }

    @Test
    void 회원가입_테스트() {
        //given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername("user");
        accountDTO.setPassword("password");
        accountDTO.setEmail("1@1");
        accountDTO.setNickname("nickname");

        // 비밀번호 암호화 가상 설정
        when(bCryptPasswordEncoder.encode(accountDTO.getPassword()))
                .thenReturn("encryptedPassword");

        // when
        userService.save(accountDTO);

        // Then
        // accountRepository의 save 메소드가 정확히 한 번 호출되었는지 검증
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
