package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDetailDTO {
    private Long id;
    private Long xp;
    private Long money;
    // Account 객체를 직접 참조하는 대신 필요한 정보만 포함할 수 있습니다.
    private Long accountId;
}
