package com.example.board.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ChatMessageDTO {

    private Long id;            // 기본키 id
    private String chatRoomId;  // 채팅방 id
    private String message;     // 메시지 내용
    private Timestamp timestamp;   // 메시지 보낸 시간
    private Long senderId;      // DTO에서는 Account 객체의 id만 가짐
    private String senderUsername; // 보낸사람 Username
    private String senderNickname; // 보낸사람 nickname

    // ----------getter setter 구현-------------------------------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
}
