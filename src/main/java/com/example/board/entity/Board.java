package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // getter setter 자동으로 생성해줌
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;
}
