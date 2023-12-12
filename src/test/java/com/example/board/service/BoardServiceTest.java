package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Account;
import com.example.board.entity.Board;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.security.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BoardServiceTest {

    @Mock /* 실제 객체를 생성하지 않고 가짜(Mock) 객체를 생성합니다. */
    private BoardRepository boardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks /* Mock 객체를 주입해야 하는 대상을 지정합니다. BoardService에 boardRepository Mock 객체를 주입합니다. */
    private BoardService boardService;

    private Account account;

    @BeforeEach /* 각 테스트 실행 전에 실행되어야 하는 메소드 */
    void setUp() {
        /* BoardServiceTest 인스턴스 내에 선언된 모든 Mockito 어노테이션(@Mock, @InjectMocks 등)을
        처리하고 초기화하는 역할을 합니다. */
        MockitoAnnotations.openMocks(this);

        // account 객체 생성
        account = new Account();
        account.setId(1L);
        account.setUsername("username");
        account.setEmail("1@1");
        account.setNickname("nickname");
        account.setPassword("encodedPassword");
        account.setRole("USER");

        // 현재 인증된 사용자 설정
        PrincipalDetails principalDetails = new PrincipalDetails(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        // SecurityContext에 Authentication 객체를 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

// 테스트 메소드------------------------------------------------------------------------------------------------------------------

    @Test
    void 글_저장_테스트() {
        // given(준비)

        // BoardDTO 객체
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle("테스트 제목");
        boardDTO.setContent("테스트 내용");
        
        // Board 객체
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setAccount(account); // setUp 메소드에서 생성된 account 객체 사용

        // boardRepository.save 호출 시 미리 정의된 board 객체를 반환하도록 스텁 설정
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when(실행)
        BoardDTO writtenBoardDTO = boardService.writeBoard(boardDTO);

        // then(검증)
        // 반환된 DTO의 제목과 내용이 기대한 값과 일치하는지 확인합니다.
        assertEquals("테스트 제목", writtenBoardDTO.getTitle());
        assertEquals("테스트 내용", writtenBoardDTO.getContent());

        // 반환된 DTO의 사용자 ID가 기대한 ID와 일치하는지 검증
        assertEquals(account.getId(), writtenBoardDTO.getAccountId());

        // 특정 메소드가 호출되었는지, 몇 번 호출되었는지, 어떤 매개변수로 호출되었는지 등을 검증할 때 사용합니다
        // 테스트가 실패하면 해당 오류 메시지를 알려줌
        // boardRepository.save가 호출되었는지 검증합니다.
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void 글_목록_불러오기_테스트() {
        // given(준비)

        // Board 객체
        Long board_Id = 1L;
        Board board = new Board();

        board.setId(board_Id);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setAccount(account); // Board 엔티티에 Account 설정

        List<Board> boardList = Collections.singletonList(board); // 단일 Board 객체를 포함하는 리스트 생성
        
        // Pageable 객체 생성
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // Page<Board> 객체 생성
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        // boardRepository.findAll 메소드가 호출되면, 가정된 boardPage를 반환하도록 설정
        when(boardRepository.findAll(pageable)).thenReturn(boardPage);

        // boardRepository.findAll 메소드가 호출되면, 가정된 boardList를 반환하도록 설정
        when(boardRepository.findAll()).thenReturn(boardList);

        // when(실행)
        Page<BoardDTO> boardDTOPage = boardService.getBoardDTOList(pageable);

        // then(검증)
        assertFalse(boardDTOPage.isEmpty()); // 반환된 리스트가 비어있지 않은지 확인
        assertEquals(board.getId(), boardDTOPage.getContent().get(0).getId()); // 리스트의 첫 번째 요소의 Id 검증
        assertEquals(board.getTitle(), boardDTOPage.getContent().get(0).getTitle()); // 리스트의 첫 번째 요소의 제목 검증
        assertEquals(board.getContent(), boardDTOPage.getContent().get(0).getContent()); // 리스트의 첫 번째 요소의 내용 검증
        assertEquals(account.getId(), boardDTOPage.getContent().get(0).getAccountId()); // 리스트의 첫 번째 요소의 account_id 검증
        assertEquals(account.getUsername(), boardDTOPage.getContent().get(0).getAccountUsername()); // 리스트의 첫 번째 요소의 accountUsername 검증
    }

    @Test
    void 특정_글_불러오기_테스트() {
        // given

        // Board 객체
        Long mockBoard_Id = 1L;

        Board mockBoard = new Board();
        mockBoard.setId(mockBoard_Id);
        mockBoard.setTitle("테스트 제목");
        mockBoard.setContent("테스트 내용");
        mockBoard.setAccount(account);

        // boardRepository.findById 메소드가 호출될 때, mockBoard 객체 반환
        when(boardRepository.findById(mockBoard_Id)).thenReturn(Optional.of(mockBoard));

        // when
        BoardDTO result = boardService.loadBoardView(mockBoard_Id);

        // Then
        assertNotNull(result);
        assertEquals(mockBoard.getId(), result.getId());
        assertEquals(mockBoard.getTitle(), result.getTitle());
        assertEquals(mockBoard.getContent(), result.getContent());

        verify(boardRepository).findById(mockBoard_Id);
    }

    @Test
    void 특정_게시글_수정_테스트() {
        //given
        Long boardId = 1L; // 가정된 게시글 ID

        // Board 객체 생성
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("기존 제목");
        board.setContent("기존 내용");
        board.setAccount(account); // Board 엔티티에 Account 설정

        // 업데이트 될 정보를 가지고 있는 boardDTO 객체
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle("수정된 제목");
        boardDTO.setContent("수정된 내용");

        // findById 호출 시 가정된 board 객체 반환 설정
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        //when
        boardService.boardEditSave(boardId, boardDTO);

        //then
        assertEquals("수정된 제목", board.getTitle());
        assertEquals("수정된 내용", board.getContent());
    }

    @Test
    void 특정_글_삭제_테스트() {
        // given
        Long boardId = 1L;

        // Board 객체 생성
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setAccount(account); // Board 엔티티에 Account 설정

        // findById 호출 시 가정된 board 객체 반환 설정
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // boardRepository의 delete 메서드가 호출될 때 아무 것도 하지 않도록 설정합니다.
        doNothing().when(boardRepository).delete(board);

        // when
        boardService.boardDelete(boardId);

        // then
        // boardRepository의 delete 메서드가 실제로 호출되었는지 검증합니다.
        verify(boardRepository).delete(board);
    }
}
