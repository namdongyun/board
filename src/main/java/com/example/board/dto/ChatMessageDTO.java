package com.example.board.dto;

import java.sql.Timestamp;

public class ChatMessageDTO {

    private Long id;            // 기본키 id
    private String chatRoomId;  // 채팅방 id
    private String message;     // 메시지 내용
    private Timestamp timestamp;   // 메시지 보낸 시간
    private Long senderId;      // DTO에서는 Account 객체의 id만 가짐


    // ----------getter setter 구현-------------------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
