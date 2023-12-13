package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessageDTO {

    private Long id;            // 기본키 id
    private String message;     // 메시지 내용
    private Long senderId;      // DTO에서는 Account 객체의 id만 가짐
    private Long chatRoomId;  // 채팅방 id
    private Timestamp timestamp;   // 메시지 보낸 시간
    private String senderUsername; // 보낸사람 Username
    private String senderNickname; // 보낸사람 nickname

    // ----------getter setter 구현-------------------------------------

}
