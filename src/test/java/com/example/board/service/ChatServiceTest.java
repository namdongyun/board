package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void 채팅_메시지_저장_테스트() {
        // given
        // Account 객체
        Long account_id = 1L;
        Account account = new Account();
        account.setId(account_id);
        account.setUsername("username");
        account.setPassword("1234");
        account.setRole("USER");
        account.setEmail("1@1");
        account.setNickname("test");

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId("room");
        chatMessageDTO.setSenderUsername("username");
        chatMessageDTO.setMessage("message");

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatMessageDTO.getChatRoomId());
        chatMessage.setMessage(chatMessageDTO.getMessage());
        chatMessage.setSender(account);

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(accountRepository.findByUsername(chatMessageDTO.getSenderUsername())).thenReturn(Optional.of(account));

        // when
        ChatMessageDTO savedChatMessageDTO = chatService.saveChatMessage(chatMessageDTO);

        // then
        assertEquals(chatMessageDTO.getMessage(), savedChatMessageDTO.getMessage());
        assertEquals(chatMessageDTO.getChatRoomId(), savedChatMessageDTO.getChatRoomId());
        assertEquals(chatMessageDTO.getSenderUsername(), savedChatMessageDTO.getSenderUsername());
    }
}
