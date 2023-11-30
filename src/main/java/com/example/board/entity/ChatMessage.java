package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 기본키 id

    @Column(name = "chat_room_id")
    private String chatRoomId;  // 채팅방 id

    @Column(name = "message")
    private String message;     // 메시지 내용

    @CreationTimestamp
    @Column(name = "timestamp")
    private Timestamp timestamp; // 메시지 보낸 시간

    @ManyToOne
    // ChatMessage 테이블의 user_id 컬럼과 Account 테이블의 id 컬럼을 통해 연결된다는 것을 의미합니다.
    // referencedColumnName: Account의 특정 사용자(id(기본키))와 연결되어 있음을 나타냅니다.
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account sender;     // 메시지를 보낸 사용자의 계정 정보
}
