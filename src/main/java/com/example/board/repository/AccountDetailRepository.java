package com.example.board.repository;

import com.example.board.entity.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {
}
