package com.example.board.service;

import com.example.board.dto.ChatRoomDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatRoom;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChatRoomServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private final String username = "user";

    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 어노테이션을 초기화합니다.

        principal = mock(Principal.class);
        // Principal 객체가 반환할 사용자 이름을 설정합니다.
        when(principal.getName()).thenReturn(username);



    }

    @Test
    public void 모든_채팅방_리스트_불러오기_테스트() {
        // given
        
        // 호스트 account 생성
        Account hostAccount1 = new Account();
        hostAccount1.setUsername("host1");
        hostAccount1.setId(100L);

        Account hostAccount2 = new Account();
        hostAccount2.setUsername("host2");
        hostAccount2.setId(200L);

        // 채팅방 생성
        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setId(1L);
        chatRoom1.setChatRoomName("테스트 채팅방 1");
        chatRoom1.setHost(hostAccount1);

        ChatRoom chatRoom2 = new ChatRoom();
        chatRoom2.setId(2L);
        chatRoom2.setChatRoomName("테스트 채팅방 2");
        chatRoom2.setHost(hostAccount2);

        when(chatRoomRepository.findAll()).thenReturn(Arrays.asList(chatRoom1, chatRoom2));

        // when
        List<ChatRoomDTO> result = chatRoomService.findAllChatRooms();

        // then
        assertEquals(2, result.size(), "채팅방 리스트의 크기가 예상과 다릅니다.");
        assertEquals("테스트 채팅방 1", result.get(0).getChatRoomName(), "첫 번째 채팅방의 이름이 예상과 다릅니다.");
        assertEquals("테스트 채팅방 2", result.get(1).getChatRoomName(), "두 번째 채팅방의 이름이 예상과 다릅니다.");
    }
}
