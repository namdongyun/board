package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 저장 처리
    public BoardDTO writeBoard(BoardDTO boardDTO) {
        Board board = new Board(); // 새로운 board (entity)객체 생성
        board.setTitle(boardDTO.getTitle());    // board 객체에 title 넣음
        board.setContent(boardDTO.getContent());// board 객체에 content 넣음

        boardRepository.save(board);    // db에 board 객체 저장 -> 알아서 바꿔주나

        return convertToDTO(board);
    }
        
    // 게시글 리스트 가져오기
    // board 앤티티 객체를 가져와서 그 안에 있는 데이터를 하나하나 boardDTO에 넣어줌
    public List<BoardDTO> getBoardDTOList() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDTO> boardDTOs = boards.stream()
                .map(board -> convertToDTO(board))
                .collect(Collectors.toList());
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
        
        return boardDTO;
    }

    // BoardDTO 객체를 Board (entity) 객체로 변환시키는 메소드
    // BoardDTO -> Board
    private Board convertToEntity(BoardDTO boardDTO) {
        Board board = new Board();
        board.setId(boardDTO.getId());
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());

        return board;
    }
}
