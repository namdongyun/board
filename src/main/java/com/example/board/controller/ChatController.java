package com.example.board.controller;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// 이 컨트롤러의 역할은 클라이언트가 보낸 메시지를 받아서 다른 모든 클라이언트에게 전달하는 것입니다.
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // @MessageMapping 어노테이션을 사용하여 클라이언트가 메시지를 보낼 수 있는 endpoint를 지정합니다.
    // "/app/chat.sendMessage" 경로로 STOMP 메시지를 보냈을 때 해당 메시지를 이 메서드로 라우팅하도록 설정합니다.
    @MessageMapping("/chat.sendMessage")

    // 메서드가 반환하는 ChatMessageDTO 객체를 "/topic/public" 경로에 구독되어 있는 모든 클라이언트에게 브로드캐스트합니다.
    @SendTo("/topic/public")

    // @Payload: 이 어노테이션은 메서드의 인자로 들어오는 ChatMessageDTO 객체를 STOMP 메시지의 본문에서 추출하여 사용하도록 합니다.
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        // DTO를 엔티티로 변환하고 데이터베이스에 저장하고 다시 저장된 DB데이터를 DTO로 변환하여 반환합니다.
        return chatService.saveChatMessage(chatMessageDTO);

        // 이 DTO는 메시지 내용, 보낸 사람, 타임스탬프 등 채팅 메시지와 관련된 정보를 포함할 수 있습니다.
    }

    // 클라이언트가 이전 대화 내역을 요청하는 엔드포인트
    @MessageMapping("/chat.history")
    @SendTo("/topic/history")
    public List<ChatMessageDTO> getChatHistory(@Payload ChatMessageDTO chatRoomIdDTO) {
        return chatService.getChatHistory(chatRoomIdDTO);
    }
}
