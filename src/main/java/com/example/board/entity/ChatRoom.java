package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 채팅방 ID

    @Column(name = "chat_room_name", nullable = false)
    private String chatRoomName; // 채팅방 이름

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt; // 채팅방 생성 시간

    // 채팅방의 방장을 나타내는 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private Account host; // 채팅방의 방장

    // 채팅방에 참여하고 있는 사용자들을 나타내는 연관 관계 설정
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chat_room_members",     // 생성될 조인 테이블의 이름을 지정
            joinColumns = @JoinColumn(name = "chat_room_id", referencedColumnName = "id"), // ChatRoom의 id 필드를 참조
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") // Account의 id 필드를 참조
    )
    private Set<Account> members = new HashSet<>(); // 채팅방의 멤버들
}
