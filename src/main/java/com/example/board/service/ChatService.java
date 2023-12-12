package com.example.board.service;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.entity.Account;
import com.example.board.entity.ChatMessage;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.ChatMessageRepository;
import com.example.board.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;



    // chatMessageDTO로 받은 데이터를 DB에 저장하고 다시 DTO로 변환하려 반환하는 메소드
    public ChatMessageDTO saveChatMessage(ChatMessageDTO chatMessageDTO, String token) {

        // DTO -> 엔티티 변환 과정
        ChatMessage chatMessage = convertToEntity(chatMessageDTO);

        // 토큰을 추출합니다.
        token = token.replace("Bearer ", "");

        // JWT 유틸리티를 사용하여 토큰에서 사용자 정보를 추출합니다.
        String username = jwtUtil.getUsername(token);

        Account currentAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 불러올 수 없습니다."));

        // chatMessage의 sender account 설정
        chatMessage.setSender(currentAccount);

        // 변환된 ChatMessage 엔티티를 DB에 저장합니다.
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        // 저장된 엔티티를 다시 DTO로 변환합니다
        // 변환된 DTO를 반환합니다.
        return convertToDTO(savedChatMessage);
    }


    // 해당 chatRoomId를 갖는 채팅방의 이전 대화 내역을 조회합니다.
    // @Payload를 통해 컨트롤러에서 DTO 객체 값에 chatRoomId를 받아옴
    public List<ChatMessageDTO> getChatHistory(ChatMessageDTO chatRoomIdDTO) {
        // 해당 chatRoomId에 해당하는 chatMessge 객체를 List형태로 모두 불러옴
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomIdDTO.getChatRoomId());

        return chatMessages.stream()
                .map(this::convertToDTO)
                .toList();
    }














    // ChatMessage -> ChatMessageDTO 로 변환 메소드
    private ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        // ChatMessageDTO 객체를 생성하고, ChatMessage의 정보를 복사합니다.
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setId(chatMessage.getId());
        chatMessageDTO.setChatRoomId(chatMessage.getChatRoomId());
        chatMessageDTO.setMessage(chatMessage.getMessage());
        chatMessageDTO.setTimestamp(chatMessage.getTimestamp());
        chatMessageDTO.setSenderId(chatMessage.getSender().getId());
        chatMessageDTO.setSenderUsername(chatMessage.getSender().getUsername());
        chatMessageDTO.setSenderNickname(chatMessage.getSender().getNickname());

        // 변환된 ChatMessageDTO 객체 반환
        return chatMessageDTO;
    }

    // ChatMessageDTO -> ChatMessage 로 변환 메소드
    private ChatMessage convertToEntity(ChatMessageDTO chatMessageDTO) {

        // ChatMessage 객체를 생성하고, ChatMessageDTO의 정보를 복사합니다.
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setChatRoomId(chatMessageDTO.getChatRoomId());
        chatMessage.setMessage(chatMessageDTO.getMessage());

        return chatMessage;
    }
}
