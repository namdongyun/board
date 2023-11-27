package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data // getter setter 자동으로 생성해줌
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // 생성 날짜와 시간
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정 날짜와 시간
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // JPA가 엔티티를 데이터베이스에 처음 저장할 때 호출
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // JPA가 엔티티를 데이터베이스에 저장하기 전에 호출
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
