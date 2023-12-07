package com.example.board.dto;

import com.example.board.entity.Account;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AccountDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String role; //ROLE_USER, ROLE_ADMIN
    private Timestamp createDate;

    // ----------getter setter 구현-------------------------------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
