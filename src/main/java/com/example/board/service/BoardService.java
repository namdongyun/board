package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Account;
import com.example.board.entity.Board;
import com.example.board.repository.AccountRepository;
import com.example.board.repository.BoardRepository;
import com.example.board.security.PrincipalDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account currentAccount = principalDetails.getAccount();

        Board board = new Board(); // 새로운 board (entity)객체 생성
        board.setTitle(boardDTO.getTitle());    // board 객체에 title 넣음
        board.setContent(boardDTO.getContent());// board 객체에 content 넣음
        board.setAccount(currentAccount); // board 객체에 글 작성자의 Account 객체를 넣음

        boardRepository.save(board);    // db에 board 객체 저장

        return convertToDTO(board);
    }
        
    // 전체 게시글 불러오기
    // board 앤티티 객체를 가져와서 그 안에 있는 데이터를 하나하나 boardDTO에 넣어줌
    public Page<BoardDTO> getBoardDTOList(Pageable pageable) {

        Page<Board> boards = boardRepository.findAll(pageable);

        // Entity를 DTO로 변환합니다.
        return boards.map(this::convertToDTO);
    }

    // 특정 게시글 불러오기
    public BoardDTO loadBoardView(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        return convertToDTO(board);
    }

    // 특정 게시글 삭제
    public void boardDelete(Long id) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

        // 게시글 작성자의 id와 현재 인증된 사용자의 id가 일치하는지 확인
        if (!board.getAccount().getId().equals(account.getId())) {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }

        // 게시글 삭제
        boardRepository.delete(board);
    }

    // 특정 게시글 수정
    public void boardEditSave(Long id, BoardDTO boardDTO) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

        // 게시글 작성자의 id와 현재 인증된 사용자의 id가 일치하는지 확인
        if (!board.getAccount().getId().equals(account.getId())) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);
    }


    // 게시글 수정 버튼 클릭
    public void boardEditBtnClick(Long id) {
        // 게시글 존재 여부 확인
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 현재 jwt 토큰으로 인증된 정보에서 account 객체 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Account account = principalDetails.getAccount();

        // 게시글 작성자의 id와 현재 인증된 사용자의 id가 일치하는지 확인
        if (!board.getAccount().getId().equals(account.getId())) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }
    }












    // Board (entity) 객체를 BoardDTO 객체로 변환시키는 메소드
    // Board -> BoardDTO
    private BoardDTO convertToDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setId(board.getId());  // 게시글 ID
        boardDTO.setTitle(board.getTitle());    // 제목
        boardDTO.setContent(board.getContent());    // 내용
        boardDTO.setAccountId(board.getAccount().getId());  // 계정 ID(기본키)
        boardDTO.setAccountUsername(board.getAccount().getUsername()); // 계정 로그인 username
        boardDTO.setAccountNickname(board.getAccount().getNickname()); // 계정 nickname
        boardDTO.setCreatedAt(board.getCreatedAt());    // 글 생성 날짜
        boardDTO.setUpdatedAt(board.getUpdatedAt());    // 글 수정 날짜

        return boardDTO;
    }

    // BoardDTO 객체를 Board (entity) 객체로 변환시키는 메소드
    // BoardDTO -> Board
    private Board convertToEntity(BoardDTO boardDTO) {
        // boardDTO 객체에 있는 account_id 값을 이용해 account_id 값을 가지고 있는 account 엔티티 객체를 가져옴
        Account account = accountRepository.findById(boardDTO.getAccountId())
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
