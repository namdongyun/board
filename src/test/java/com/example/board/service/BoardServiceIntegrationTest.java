package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class BoardServiceIntegrationTest {

    @Autowired private BoardService boardService;
    @Autowired private BoardRepository boardRepository;

    // BoardDTO를 Board 타입으로 변환 하는 메소드
    public Board convertToEntity(BoardDTO boardDTO) {
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        // 필요한 나머지 필드도 설정
        return board;
    }

    // 서비스에 writeBoard() 테스트
    @Test
    public void 게시글_생성_테스트() {
        // given(준비)
        BoardDTO boardDTO = new BoardDTO(); // 새로운 boardDTO (entity)객체 생성
        boardDTO.setTitle("제목");
        boardDTO.setContent("내용");

        // when(실행)
        BoardDTO writtenBoardDTO = boardService.writeBoard(boardDTO);
        Long saveId = writtenBoardDTO.getId();

        // then(검증)
        // 반환된 saveId가 null이 아닌지 확인
        assertNotNull(saveId);
        System.out.println(saveId);

        // mockBoardRepository를 사용하여 ID로 저장된 게시글을 조회
        Board savedBoard = boardRepository.findById(saveId).orElse(null);

        // 저장된 게시글이 null이 아닌지 확인
        assertNotNull(savedBoard);

        // 저장된 게시글의 제목과 내용이 예상한 값과 일치하는지 확인
        assertEquals("제목", savedBoard.getTitle());
        assertEquals("내용", savedBoard.getContent());
    }
}
