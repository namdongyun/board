package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatRoomDTO {
    private Long id; // 채팅방 id
    private String chatRoomName; // 채팅방 이름
    private Timestamp createdAt; // 채팅방 생성 시간
    private Long hostId; // 방장 id
}
