package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BoardDTO boardWritePro(@RequestBody BoardDTO boardDTO, Authentication authentication) {

        return boardService.writeBoard(boardDTO, authentication);
    }

    // 게시글 리스트 가져오기
    @GetMapping("/api/board/list")
    public List<BoardDTO> boardList() {

        // BoardService를 통해 boardDTOs(엔티티 -> DTO 변환된) 리스트를 React 서버로 보내줌
        return boardService.getBoardDTOList(); 
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
        boardService.deleteBoard(id);
    }

//    // 게시글 수정 버튼 클릭 시 수정 페이지로 이동
//    @GetMapping("/board/editPage")
//    public String boardEdit(Model model, @RequestParam("id") Long id) {
//        // boardService를 통해 id에 해당하는 BoardDTO 객체를 가져옴
//        BoardDTO boardDTO = boardService.loadBoardView(id);
//
//        // 가져온 BoardDTO 객체를 모델에 추가, 이 모델은 뷰에 전달됨
//        model.addAttribute("boardDTO", boardDTO);
//        return "board/boardEdit";
//    }

    // 수정 페이지에서 수정 완료 버튼
    @PutMapping("/api/board/update/{id}")
    public void boardEditSave(@PathVariable Long id,
                              @RequestBody BoardDTO boardDTO, Authentication authentication) {

        boardService.updateBoard(id, boardDTO, authentication);
    }
}
