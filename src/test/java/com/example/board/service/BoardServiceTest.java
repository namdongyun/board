package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Account;
import com.example.board.entity.Board;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1111");

        // Account 객체
        Long account_id = 1L;
        Account account = new Account();
        account.setId(account_id);
        account.setUsername("1111");
        account.setPassword("1234");
        account.setRole("USER");
        account.setEmail("1@1");

        // BoardDTO 객체
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle("테스트 제목");
        boardDTO.setContent("테스트 내용");
        boardDTO.setAccount_id(account_id);
        
        // Board 객체
        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setAccount(account);

        // Account 조회를 위한 스텁 설정
        when(accountRepository.findByUsername("1111")).thenReturn(Optional.of(account));

        /* boardRepository.save 메소드가 호출될 때 어떤 Board 클래스의 인스턴스가 인자로 주어지더라도,
         실제 boardRepository.save 메소드가 실행되지 않고 mock의 boardRepository.save 메소드를 사용해
         미리 정의된 board 객체를 결과로 반환하도록 설정합니다. */
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when(실행)
        BoardDTO writtenBoardDTO = boardService.writeBoard(boardDTO, authentication);

        // then(검증)
        // 반환된 ID가 null이 아닌지 확인합니다.
        assertNotNull(writtenBoardDTO.getTitle());
        assertNotNull(writtenBoardDTO.getContent());

        // 반환된 DTO의 사용자 ID가 기대한 ID와 일치하는지 검증
        assertEquals(account_id, writtenBoardDTO.getAccount_id());

        // 특정 메소드가 호출되었는지, 몇 번 호출되었는지, 어떤 매개변수로 호출되었는지 등을 검증할 때 사용합니다
        // 테스트가 실패하면 해당 오류 메시지를 알려줌
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void 글_목록_불러오기_테스트() {
        // given(준비)
        // Account 객체
        Long account_id = 100L; // 가정된 사용자 ID
        String account_username = "username";
        Account account = new Account();
        account.setId(account_id);
        account.setUsername(account_username);

        // Board 객체
        Long board_Id = 1L;
        Board board = new Board();
        board.setId(board_Id);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setAccount(account); // Board 엔티티에 Account 설정

        List<Board> boardList = Collections.singletonList(board); // 단일 Board 객체를 포함하는 리스트 생성

        // boardRepository.findAll 메소드가 호출되면, 가정된 boardList를 반환하도록 설정
        when(boardRepository.findAll()).thenReturn(boardList);

        // when(실행)
        List<BoardDTO> boardDTOList = boardService.getBoardDTOList();

        // then(검증)
        assertFalse(boardDTOList.isEmpty()); // 반환된 리스트가 비어있지 않은지 확인
        assertEquals(board.getId(), boardDTOList.get(0).getId()); // 리스트의 첫 번째 요소의 Id 검증
        assertEquals(board.getTitle(), boardDTOList.get(0).getTitle()); // 리스트의 첫 번째 요소의 제목 검증
        assertEquals(board.getContent(), boardDTOList.get(0).getContent()); // 리스트의 첫 번째 요소의 내용 검증
        assertEquals(account_id, boardDTOList.get(0).getAccount_id()); // 리스트의 첫 번째 요소의 account_id 검증
        assertEquals(account_username, boardDTOList.get(0).getAccount_username()); // 리스트의 첫 번째 요소의 account_username 검증
    }

    @Test
    void 글_수정_테스트() {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1111");

        Long account_id = 100L; // 가정된 사용자 ID
        String account_username = "1111";

        Account account = new Account();
        account.setId(account_id);
        account.setUsername(account_username);

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

        // boardRepository.findById 메소드가 호출될 때, id 값을 넣고 Optional<Board> 반환하도록 설정
        when(boardRepository.findById(id)).thenReturn(Optional.of(beforeBoard));

        // AccountRepository의 findByUsername 메소드가 호출될 때, account_username 값을 넣고 Optional<Account> 반환하도록 설정
        when(accountRepository.findByUsername(account_username)).thenReturn(Optional.of(account));

        //when
        boardService.updateBoard(id, boardDTO, authentication);

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
