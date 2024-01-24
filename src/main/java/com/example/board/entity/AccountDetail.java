package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data // Lombok을 사용하여 getter와 setter를 자동으로 생성합니다.
@Table(name = "user_detail")
public class AccountDetail {
    @Id
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long xp; // 사용자의 xp을 나타냅니다.

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long money; // 사용자의 돈을 나타냅니다.

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long titleLevel; // 사용자의 돈을 나타냅니다.

    @OneToOne
    @MapsId // AccountDetail의 ID가 Account의 ID와 동일하게 됩니다. 이는 외래 키를 별도로 유지할 필요 없이 두 엔티티 간의 관계를 단순화시켜 줍니다.
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Account account;
}
