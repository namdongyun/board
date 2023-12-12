package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ChatService chatService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // account 객체 생성
        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setEmail("1@1");
        account.setNickname("nickname");
        account.setPassword("encodedPassword");
        account.setRole("USER");
    }


    @Test
    void 채팅_메시지_저장_테스트() {
        // given(준비)
        String rawToken = "Bearer some.jwt.token";
        String token = rawToken.replace("Bearer ", "");
        String username = "testUser";

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId("room");
        chatMessageDTO.setSenderUsername("username");
        chatMessageDTO.setMessage("message");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatMessageDTO.getChatRoomId());
        chatMessage.setMessage(chatMessageDTO.getMessage());
        chatMessage.setSender(account);

        // 토큰에서 사용자 이름을 추출하는 과정을 스텁으로 설정
        when(jwtUtil.getUsername(token)).thenReturn(username);

        // accountRepository에서 Account를 찾는 과정을 스텁으로 설정
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));

        // // 저장할 때 반환될 ChatMessage의 스텁 설정
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        // when
        ChatMessageDTO savedChatMessageDTO = chatService.saveChatMessage(chatMessageDTO, rawToken);

        // then
        assertNotNull(savedChatMessageDTO);
        assertEquals(account, chatMessage.getSender());
        verify(chatMessageRepository).save(chatMessage);
        verify(jwtUtil).getUsername(token);
        verify(accountRepository).findByUsername(username);
    }
}
