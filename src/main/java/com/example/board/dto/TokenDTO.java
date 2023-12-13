package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
