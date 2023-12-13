package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.entity.ChatRoom;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.repository.ChatRoomRepository;
import com.example.board.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;



    // chatMessageDTO로 받은 데이터를 DB에 저장하고 다시 DTO로 변환하려 반환하는 메소드
    @Transactional
    public ChatMessageDTO saveChatMessage(Long roomId, ChatMessageDTO chatMessageDTO, Principal principal) {

        // 현재 ChatRoom 객체 추출
        ChatRoom currentChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // principal 으로 현재 사용자 Account 객체 추출
        Account currentAccount = accountRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // currentAccount가 currentChatRoom의 members에 포함되어 있는지 확인
        boolean isMember = currentChatRoom.getMembers().contains(currentAccount);
        if (!isMember) {
            throw new IllegalArgumentException("현재 사용자가 해당 채팅방의 멤버가 아닙니다.");
        }

        // DTO -> 엔티티 변환 과정
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatMessageDTO.getMessage());    // message
        chatMessage.setChatRoom(currentChatRoom);   // ChatRoom 객체
        chatMessage.setSender(currentAccount);      // Account 객체

        // 변환된 ChatMessage 엔티티를 DB에 저장합니다.
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // 저장된 엔티티를 다시 DTO로 변환합니다
        // 변환된 DTO를 반환하여 컨트롤러에서 @SendTo 경로에 구독되어 있는 모든 클라이언트에게 메시지를 브로드캐스트 합니다.
        return convertToDTO(savedChatMessage);
    }


    // 해당 chatRoomId를 갖는 채팅방의 이전 대화 내역을 조회합니다.
    // 메소드 전체를 하나의 트랜잭션으로 처리합니다.
    // 이렇게 하면 메소드가 종료될 때까지 데이터베이스 세션이 유지되어 지연 로딩 문제를 해결할 수 있습니다.
    @Transactional
    public List<ChatMessageDTO> getChatHistory(Long roomId, Principal principal) {
        // 현재 ChatRoom 객체 추출
        ChatRoom currentChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // principal 으로 현재 사용자 Account 객체 추출
        Account currentAccount = accountRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // currentAccount가 currentChatRoom의 members에 포함되어 있는지 확인
        boolean isMember = currentChatRoom.getMembers().contains(currentAccount);
        if (!isMember) {
            throw new IllegalArgumentException("현재 사용자가 해당 채팅방의 멤버가 아닙니다.");
        }

        // roomId에 해당하는 chatMessge 객체를 List형태로 모두 불러옴
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(roomId);

        // DB에서 받은 ChatMessage 리스트를 ChatMessageDTO로 변환합니다.

        return chatMessages.stream()
                .map(this::convertToDTO)
                .toList();
    }














    // ChatMessage -> ChatMessageDTO 로 변환 메소드
    private ChatMessageDTO convertToDTO(ChatMessage chatMessage) {

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();

        chatMessageDTO.setId(chatMessage.getId());                              // 기본키 id
        chatMessageDTO.setMessage(chatMessage.getMessage());                    // 메시지
        chatMessageDTO.setTimestamp(chatMessage.getSentAt());                   // 메시지 생성 시간
        chatMessageDTO.setChatRoomId(chatMessage.getChatRoom().getId());        // ChatRoom 엔티티 기본키 id
//        chatMessageDTO.setSenderId(chatMessage.getSender().getId());            // Account 엔티티 기본키 id
//        chatMessageDTO.setSenderUsername(chatMessage.getSender().getUsername());// Account 엔티티 username
        // FetchType.LAZY로 인해 DB가 이미 닫혀있는 상태에서 Account 객체를 가져오려고
        chatMessageDTO.setSenderNickname(chatMessage.getSender().getNickname());// Account 엔티티 nickname

        // 변환된 ChatMessageDTO 객체 반환
        return chatMessageDTO;
    }

    // ChatMessageDTO -> ChatMessage 로 변환 메소드
    private ChatMessage convertToEntity(ChatMessageDTO chatMessageDTO) {

        // ChatMessage 객체를 생성하고, ChatMessageDTO의 정보를 복사합니다.
        ChatMessage chatMessage = new ChatMessage();
        
        chatMessage.setMessage(chatMessageDTO.getMessage());

        return chatMessage;
    }
}
