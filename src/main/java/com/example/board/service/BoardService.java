package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Account;
import com.example.board.entity.Board;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, AccountRepository accountRepository) {
        this.boardRepository = boardRepository;
        this.accountRepository = accountRepository;
    }

    // 게시글 저장 처리
    public BoardDTO writeBoard(BoardDTO boardDTO) {

        // 현재 인증된 사용자의 Authentication 객체를 가져옵니다.
        // 이 객체는 현재 로그인한 사용자의 보안 관련 세부 정보를 포함하고 있습니다.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        // 현재 인증된 사용자의 username 을 문자열로 저장
        String currentPrincipalName = authentication.getName();

        // 사용자의 username 으로 Account 엔티티 조회
        Account currentAccount = accountRepository.findByUsername(currentPrincipalName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 username 값을 불러올 수 없습니다."));

        Board board = new Board(); // 새로운 board (entity)객체 생성
        board.setTitle(boardDTO.getTitle());    // board 객체에 title 넣음
        board.setContent(boardDTO.getContent());// board 객체에 content 넣음
        board.setAccount(currentAccount); // board 객체에 글 작성자의 Account 객체를 넣음

        boardRepository.save(board);    // db에 board 객체 저장

        return convertToDTO(board);
    }
        
    // 전체 게시글 불러오기
    // board 앤티티 객체를 가져와서 그 안에 있는 데이터를 하나하나 boardDTO에 넣어줌
    public List<BoardDTO> getBoardDTOList() {
        List<Board> boards = boardRepository.findAll();

        List<BoardDTO> boardDTOs = new ArrayList<>();

        // boards 리스트에서 한개씩 꺼내 -> board에 넣기 반복
        for (Board board : boards) {
            BoardDTO boardDTO = convertToDTO(board);
            boardDTOs.add(boardDTO);
        }
        return boardDTOs;
    }

    // 특정 게시글 불러오기
    public BoardDTO loadBoardView(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        return convertToDTO(board);
    }

    // 특정 게시글 삭제
    public void deleteBoard(Long id) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        // 게시글 삭제
        boardRepository.delete(board);
    }

    // 특정 게시글 수정

    public void updateBoard(Long id, BoardDTO boardDTO) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);
    }












    // Board (entity) 객체를 BoardDTO 객체로 변환시키는 메소드
    // Board -> BoardDTO
    private BoardDTO convertToDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setId(board.getId());
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());
        boardDTO.setAccount_id(board.getAccount().getId());
        boardDTO.setAccount_username(board.getAccount().getUsername());

        return boardDTO;
    }

    // BoardDTO 객체를 Board (entity) 객체로 변환시키는 메소드
    // BoardDTO -> Board
    private Board convertToEntity(BoardDTO boardDTO) {
        // boardDTO 객체에 있는 account_id 값을 이용해 account_id 값을 가지고 있는 account 엔티티 객체를 가져옴
        Account account = accountRepository.findById(boardDTO.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Board board = new Board();
        board.setId(boardDTO.getId());
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        // board와 외래키로 연결되어있는 account 엔티티 객체를 넣어줌
        board.setAccount(account);

        return board;
    }
}
