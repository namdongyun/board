package com.example.board.repository;

import com.example.board.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // chatRoomId에 해당하는 대화내역 모두 불러오기
    List<ChatMessage> findAllByChatRoomId(Long roomId);

    @Transactional
    void deleteByChatRoomId(Long roomId);

}
