package com.example.board.controller;

import com.example.board.dto.ChatMessageDTO;
import com.example.board.dto.ChatRoomDTO;
import com.example.board.entity.ChatMessage;
import com.example.board.service.ChatMessageService;
import com.example.board.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
// 이 컨트롤러의 역할은 클라이언트가 보낸 메시지를 받아서 다른 모든 클라이언트에게 전달하는 것입니다.
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;


    // 채팅방 목록 조회
    @GetMapping("/api/chatrooms/list")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms() {
        // 채팅방 목록을 조회하는 서비스 로직을 호출
        List<ChatRoomDTO> chatRoomDTOs = chatRoomService.findAllChatRooms();
        return ResponseEntity.ok(chatRoomDTOs);
    }

    // 채팅방 생성
    @PostMapping("/api/chatrooms/create")
    public ResponseEntity<ChatRoomDTO> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        ChatRoomDTO createdChatRoomDTO = chatRoomService.createChatRoom(chatRoomDTO);

        return ResponseEntity.ok(createdChatRoomDTO);
    }

    // 채팅방 삭제
    @DeleteMapping("/api/chatrooms/delete/{roomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.ok().build();
    }

    // @MessageMapping 어노테이션을 사용하여 클라이언트가 메시지를 보낼 수 있는 endpoint를 지정합니다.
    // "/app/{roomId}/sendMessage" 경로로 STOMP 메시지를 보냈을 때 해당 메시지를 이 메서드로 라우팅하도록 설정합니다.
    @MessageMapping("/{roomId}/sendMessage")
    // 메서드가 반환하는 ChatMessageDTO 객체를 "/room/{roomId}" 경로에 구독되어 있는 모든 클라이언트에게 브로드캐스트합니다.
    @SendTo("/room/{roomId}")
    // @Payload: 이 어노테이션은 메서드의 인자로 들어오는 ChatMessageDTO 객체를 STOMP 메시지의 본문에서 추출하여 사용하도록 합니다.
    public ChatMessageDTO sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageDTO chatMessageDTO, @Header("Authorization") String rawToken) {

        // DTO를 엔티티로 변환하고 데이터베이스에 저장하고 다시 저장된 DB데이터를 DTO로 변환하여 반환합니다.
        return chatMessageService.saveChatMessage(roomId, chatMessageDTO, rawToken);
    }

    // {roomId} 채팅방에 입장하는 메서드입니다.
//    @MessageMapping("/{roomId}/join")
//    @SendTo("/room/{roomId}/joinMessage")
    @SubscribeMapping("/{roomId}/joinMessage")
    public ChatMessageDTO joinRoom(@DestinationVariable Long roomId, @Header("Authorization") String rawToken) {
        return chatRoomService.joinRoom(roomId, rawToken);
    }

    // 클라이언트가 {roomId}방의 이전 대화 내역을 요청하는 엔드포인트
    @MessageMapping("/{roomId}/history")
    @SendTo("/room/{roomId}/history")
    public List<ChatMessageDTO> getChatHistory(@DestinationVariable Long roomId, @Header("Authorization") String rawToken) {
        return chatMessageService.getChatHistory(roomId, rawToken);
    }
}
