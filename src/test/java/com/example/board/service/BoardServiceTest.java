package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BoardServiceTest {


    private BoardService boardService;
    private BoardRepository mockBoardRepository;
    private BoardDTO boardDTO;

    // 테스트 하기 전 셋업
    @BeforeEach
    public void setUp() {
        mockBoardRepository = mock(BoardRepository.class);
        boardService = new BoardService(mockBoardRepository);

        when(mockBoardRepository.save(any(Board.class))).then(invocation -> {
            Board board = invocation.getArgument(0);
            // 테스트를 위한 임의의 ID 설정
            board.setId(1L); // 예시로 1L을 설정함
            return board;
        });

        when(mockBoardRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Board board = new Board();
            board.setId(id);
            board.setTitle("제목");
            board.setContent("내용");
            return Optional.of(board);
        });
    }

    // BoardDTO를 Board 타입으로 변환 하는 메소드
    public Board convertToEntity(BoardDTO boardDTO) {
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        // 필요한 나머지 필드도 설정
        return board;
    }

    // 서비스에 write() 테스트
    @Test
    public void 게시글_생성_테스트() {
        // given(준비)
        BoardDTO boardDTO = new BoardDTO(); // 새로운 boardDTO (entity)객체 생성
//        boardDTO.setTitle("제목");
//        boardDTO.setContent("내용");

        // when(실행)
        Long saveId = boardService.write(boardDTO);

        // then(검증)
        // 반환된 saveId가 null이 아닌지 확인
        assertNotNull(saveId);
        System.out.println(saveId);

        // mockBoardRepository를 사용하여 ID로 저장된 게시글을 조회
        Board savedBoard = mockBoardRepository.findById(saveId).orElse(null);

        // 저장된 게시글이 null이 아닌지 확인
        assertNotNull(savedBoard);

        // 저장된 게시글의 제목과 내용이 예상한 값과 일치하는지 확인
        assertEquals("제목", savedBoard.getTitle());
        assertEquals("내용", savedBoard.getContent());
    }
}
