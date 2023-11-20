package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BoardServiceTest {

    @Mock /* 실제 객체를 생성하지 않고 가짜(Mock) 객체를 생성합니다. */
    private BoardRepository boardRepository;

    @InjectMocks /* Mock 객체를 주입해야 하는 대상을 지정합니다. BoardService에 boardRepository Mock 객체를 주입합니다. */
    private BoardService boardService;

    @BeforeEach /* 각 테스트 실행 전에 실행되어야 하는 메소드 */
    void setUp() {
        /* BoardServiceTest 인스턴스 내에 선언된 모든 Mockito 어노테이션(@Mock, @InjectMocks 등)을
        처리하고 초기화하는 역할을 합니다. */
        MockitoAnnotations.openMocks(this);
    }

// 테스트 메소드------------------------------------------------------------------------------------------------------------------

    @Test
    void 글_저장_테스트() {
        // given(준비)
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle("테스트 제목");
        boardDTO.setContent("테스트 내용");

        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());

        /* boardRepository.save 메소드가 호출될 때 어떤 Board 클래스의 인스턴스가 인자로 주어지더라도,
         실제 boardRepository.save 메소드가 실행되지 않고 mock의 boardRepository.save 메소드를 사용해
         미리 정의된 board 객체를 결과로 반환하도록 설정합니다. */
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        System.out.println();

        // when(실행)
        BoardDTO writtenBoardDTO = boardService.writeBoard(boardDTO);

        // then(검증)
        // 반환된 ID가 null이 아닌지 확인합니다.
        assertNotNull(writtenBoardDTO.getTitle());
        assertNotNull(writtenBoardDTO.getContent());

        // 특정 메소드가 호출되었는지, 몇 번 호출되었는지, 어떤 매개변수로 호출되었는지 등을 검증할 때 사용합니다
        // 테스트가 실패하면 해당 오류 메시지를 알려줌
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void 글_목록_불러오기_테스트() {
        // given
        Long id = 1L;
        Board board = new Board();

        board.setId(id);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");

        // 위에서 생성한 Board 객체를 담은 Optional을 반환하도록 Mockito를 사용하여 스텁을 설정
        // findById 메소드는 일반적으로 결과가 없을 경우를 대비하여 결과를 Optional로 감싸서 반환하는 것이 일반적입니다.
        when(boardRepository.findById(id)).thenReturn(Optional.of(board));

        // when
        BoardDTO boardDTO = boardService.loadBoardView(id);

        // then
        assertNotNull(boardDTO);

        // BoardDTO의 ID가 기대한 ID와 일치하는지 검증합니다.
        assertEquals(id, boardDTO.getId());
        assertEquals("테스트 제목", boardDTO.getTitle());
        assertEquals("테스트 내용", boardDTO.getContent());
    }

    @Test
    void 글_수정_테스트() {
        //given
        Long id = 1L;

        // 업데이트 될 정보를 가지고 있는 boardDTO 객체
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setTitle("수정된 제목");
        boardDTO.setContent("수정된 내용");

        // 업데이트 전 board 객체
        Board beforeBoard = new Board();
        beforeBoard.setId(id);
        beforeBoard.setTitle("테스트 제목");
        beforeBoard.setContent("테스트 내용");

        // boardRepository.findById(id) 메소드 반환 값 Optional 객체로 설정
        when(boardRepository.findById(id)).thenReturn(Optional.of(beforeBoard));

        //when
        boardService.updateBoard(id, boardDTO);

        //then
        assertEquals("수정된 제목", beforeBoard.getTitle());
        assertEquals("수정된 내용", beforeBoard.getContent());

    }

    @Test
    void 글_삭제_테스트() {
        // given
        Long id = 1L;
        Board board = new Board();

        board.setId(id);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");

        when(boardRepository.findById(id)).thenReturn(Optional.of(board));
        doNothing().when(boardRepository).delete(board);

        // when
        boardService.deleteBoard(id);

        // then
        verify(boardRepository).delete(board);
    }
}
