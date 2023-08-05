package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.dto.CommentDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.CommentService;
import com.ssafy.tab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    //게시판에 접속 시 전체 게시글 가져오기
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> board(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        //파라미터로 설정 가능 한 것
        ///board?page=0&size=3&sort=id,desc&sort=username,dec

        /*
        로그인 하지 않았을 시 접근 불가
         System.out.println(request.getHeader("TOKEN"));
         */

        try {
            resultMap.put("code", "500");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러왔습니다.");
            resultMap.put("data", boardService.board(pageable));
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러오지 못했습니다!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }


    //게시글 등록
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registBoard(@RequestBody BoardDto boardDto ,HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();

        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
        boardDto.setUserId(userId);
         */

        boardDto.setUserId(2L);
        boardDto.setCreateTime(LocalDateTime.now());
        try{
            BoardDto newBoardDto = boardService.registBoard(boardDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 등록 완료!");
            resultMap.put("data", newBoardDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 등록 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("boardId")Long boardId, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        토큰으로 유저를 탐지해서 현재 board의 글 작성자가 맞는지 확인하기
        User user = new User();
        userService.joinUser(user);
        Board findBoard = boardService.findBoard(boardId);
        if(findBoard.getUser().getId() != user.getId()){
            resultMap.put("code", "401");
            resultMap.put("msg", "삭제 권한이 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
         */
        try {
            boardService.deleteBoard(boardId);
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 삭제 완료!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 삭제 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> modifyBoard(BoardDto boardDto, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        토큰으로 유저를 탐지해서 현재 board의 글 작성자가 맞는지 확인하기
        User user = new User();
        userService.joinUser(user);
        Board findBoard = boardService.findBoard(boardId);
        if(findBoard.getUser().getId() != user.getId()){
            resultMap.put("code", "401");
            resultMap.put("msg", "삭제 권한이 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
         */
        try {
            BoardDto newBoardDto = boardService.modifyBoard(boardDto);
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 수정 완료!");
            resultMap.put("data", newBoardDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 수정 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 자세히보기
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> boardDetail(@PathVariable("boardId") Long boardId ,HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
        boardDto.setUserId(userId);
         */
        try{
            BoardDto newBoardDto = boardService.boardDetail(boardId);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 불러오기 완료!");
            resultMap.put("data", newBoardDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 불러오기 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 머리말대로 검색하기 + 페이징 처리
    @GetMapping("/sort/{sort}")
    public ResponseEntity<Map<String, Object>> boardBySort(@PathVariable("sort") Sort sort, Pageable pageable, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
        boardDto.setUserId(userId);
         */
        try{
            Page<BoardDto> pageList = boardService.boardBySort(sort, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 머리말로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 머리말로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 작성자로 검색하기 + 페이징 처리
    @GetMapping("/user/{userName}")
    public ResponseEntity<Map<String, Object>> boardByUser(@PathVariable("userName") String userName, Pageable pageable, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        try{
            Page<BoardDto> pageList = boardService.boardByUser(userName, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 작성자로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 작성자로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 제목으로 검색하기 + 페이징 처리
    @GetMapping("/title/{title}")
    public ResponseEntity<Map<String, Object>> boardByTitle(@PathVariable("title") String title, Pageable pageable, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        try{
            Page<BoardDto> pageList = boardService.boardByTitle(title, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 제목으로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 제목으로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 내용으로로 검색하기 + 페이징 처리
    @GetMapping("/content/{content}")
    public ResponseEntity<Map<String, Object>> BoardByContent(@PathVariable("content") String content, Pageable pageable, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        try{
            Page<BoardDto> pageList = boardService.boardByContent(content, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 내용으로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 내용으로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 작성하기
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Map<String, Object>> registComment(@PathVariable("boardId")Long boardId, @RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        commentDto.setUserId(2L);
        commentDto.setBoardId(boardId);
        commentDto.setCreateTime(LocalDateTime.now());
        System.out.println(commentDto.getContent());
        try{
            CommentDto newCommentDto = commentService.registComment(commentDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 작성 성공!");
            resultMap.put("data", newCommentDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 작성 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 삭제하기
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId")Long commentId, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        try{
            commentService.deleteComment(commentId);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 삭제 성공!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 삭제 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 수정하기
    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        /*
        boardDto에 user를 token으로 찾아서 담기.
        Long userId =;
         */
        commentDto.setCreateTime(LocalDateTime.now());
        try{
            CommentDto newCommentDto = commentService.updateComment(commentDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 수정 성공!");
            resultMap.put("data", newCommentDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 수정 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }
}
