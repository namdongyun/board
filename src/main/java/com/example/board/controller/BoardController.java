package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 글 작성 저장 처리
    @PostMapping("/api/board/write")
    // html에 있는 input name="title" 과 textarea name="content"를 받아서
    // 메소드 매개변수인 BoardDTO 객체의 title, content 넣어줌
    public BoardDTO boardWritePro(@RequestBody BoardDTO boardDTO) {

        return boardService.writeBoard(boardDTO);
    }

    // 게시글 리스트 가져오기
    @GetMapping("/api/board/list")
    public Page<BoardDTO> boardList(Pageable pageable) {

        // BoardService를 통해 boardDTOs(엔티티 -> DTO 변환된) 리스트를 React 서버로 보내줌
        return boardService.getBoardDTOList(pageable);
    }

    // 제목 누르면 나오는 게시글 상세 페이지
    @GetMapping("/api/board/view/{id}")
    public BoardDTO boardView(@PathVariable Long id) {

        // BoardDTO 객체를 React 서버로 보내줌
        return boardService.loadBoardView(id);
    }

    // 게시글 삭제 버튼 클릭 시
    @DeleteMapping("/api/board/delete/{id}")
    public void boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
    }

    // 글 수정 하기 버튼
    @GetMapping("/api/board/updateBtn/{id}")
    public void boardEditBtnClick(@PathVariable Long id) {
        boardService.boardEditBtnClick(id);
    }

    // 수정 완료 버튼
    @PutMapping("/api/board/update/{id}")
    public void boardEditSave(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {

        boardService.boardEditSave(id, boardDTO);
    }

}
