package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data // getter setter 자동으로 생성해줌
@Table(name = "user")
public class Account {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    private Timestamp create_date;
}
