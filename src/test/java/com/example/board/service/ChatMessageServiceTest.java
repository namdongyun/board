package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.entity.ChatRoom;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.repository.ChatRoomRepository;
import com.example.board.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatMessageServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private final Long roomId = 1L;
    private final String rawToken = "Bearer someTokenString";
    private final String username = "testUser";

    private ChatRoom chatRoom;
    private Account account;
    private ChatMessageDTO chatMessageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 채팅방 설정
        chatRoom = new ChatRoom();
        chatRoom.setId(roomId);
        chatRoom.setChatRoomName("Test Chat Room");

        // account 객체 생성
        account = new Account();
        account.setUsername(username);

        // 채팅방에 사용자 추가
        chatRoom.setMembers(Set.of(account));

        // ChatMessageDTO 설정
        chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setMessage("Test Message");

        // 토큰에서 사용자 이름을 추출하는 과정을 스텁으로 설정
        when(jwtUtil.getUsername(any())).thenReturn(username);
    }


    @Test
    void 채팅_메시지_저장_테스트() {
        // given(준비)
        when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));
        when(accountRepository.findByUsername(any())).thenReturn(Optional.of(account));

        // 메소드 호출 시 전달된 첫 번째 인자를 반환하도록 합니다. 즉, 이 경우에는 save 메소드에 전달된 ChatMessage 객체 자체를 반환하게 됩니다.
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        ChatMessageDTO result = chatMessageService.saveChatMessage(roomId, chatMessageDTO, rawToken);

        // then
        assertNotNull(result);
        assertEquals(chatMessageDTO.getMessage(), result.getMessage());
        verify(chatRoomRepository).findById(roomId);
        verify(accountRepository).findByUsername(username);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
}
