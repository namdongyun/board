package com.example.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private Long accountId;
    private String accountUsername;
    private String accountNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----------getter setter 구현-------------------------------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public void setAccountNickname(String accountNickname) {
        this.accountNickname = accountNickname;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
