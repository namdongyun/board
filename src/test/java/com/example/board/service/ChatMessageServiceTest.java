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
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatMessageServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private final Long roomId = 1L;
    private final String username = "user";

    private ChatRoom chatRoom;
    private Account account;
    private ChatMessageDTO chatMessageDTO;
    private Principal principal;
    private List<ChatMessage> chatMessageList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        principal = mock(Principal.class);

        // 채팅방 설정
        chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        // account 객체 생성
        account = new Account();
        account.setUsername(username);

        // 채팅방에 사용자 추가
        chatRoom.setMembers(Set.of(account));

        // ChatMessageDTO 설정
        chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setMessage("Test Message");

        // Principal 객체가 반환할 사용자 이름을 설정합니다.
        when(principal.getName()).thenReturn(username);

        // Repository에서 반환할 객체들을 설정합니다.
        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(chatRoom));
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        // 메소드 호출 시 전달된 첫 번째 인자를 반환하도록 합니다. 즉, 이 경우에는 save 메소드에 전달된 ChatMessage 객체 자체를 반환하게 됩니다.
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }


    @Test
    void 채팅_메시지_저장_테스트() {
        // given(준비)

        // when
        ChatMessageDTO result = chatMessageService.saveChatMessage(roomId, chatMessageDTO, principal);

        // then
        assertNotNull(result, "결과 DTO는 null이 아니어야 합니다.");
        assertEquals(chatMessageDTO.getMessage(), result.getMessage(), "메시지 내용이 일치해야 합니다.");

        // 저장 메소드가 호출되었는지 검증
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }

    @Test
    void getChatHistory_존재하는_채팅방_멤버인_경우() {
        // 준비
        chatMessageList = new ArrayList<>();

        // 채팅 메시지 목록에 메시지를 추가합니다.
        ChatMessage message1 = new ChatMessage();
        message1.setMessage("첫 번째 메시지");
        message1.setChatRoom(chatRoom);
        message1.setSender(account);

        ChatMessage message2 = new ChatMessage();
        message2.setMessage("두 번째 메시지");
        message2.setChatRoom(chatRoom);
        message2.setSender(account);

        chatMessageList.add(message1);
        chatMessageList.add(message2);

        when(chatMessageRepository.findAllByChatRoomId(roomId)).thenReturn(chatMessageList);


        // 실행
        List<ChatMessageDTO> result = chatMessageService.getChatHistory(roomId, principal);

        // 검증
        assertNotNull(result, "결과는 null이 아니어야 합니다.");
        assertEquals(chatMessageList.size(), result.size(), "대화 내역의 크기가 일치해야 합니다.");

        // ChatMessage 리스트를 ChatMessageDTO 리스트로 변환하는 과정도 검증합니다.
        for (int i = 0; i < result.size(); i++) {
            assertEquals(chatMessageList.get(i).getMessage(), result.get(i).getMessage(), "메시지 내용이 일치해야 합니다.");
        }
    }
}
