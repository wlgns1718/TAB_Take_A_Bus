package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.dto.CommentDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.CommentService;
import com.ssafy.tab.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tab/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;

    //게시판에 접속 시 전체 게시글 가져오기
    @ApiOperation(value = "게시판", notes = "게시판에 접속 했을 때 전체 글을 가져온다.", response = Map.class)
    @GetMapping("")
    //파라미터로 설정 가능 한 것
    ///board?page=0&size=3&sort=id,desc&sort=username,dec
    public ResponseEntity<Map<String, Object>> board(@ApiParam(value = "페이징처리에 필요한 정보", required = true) Pageable pageable, Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("code", "500");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러왔습니다.");
            resultMap.put("data", boardService.board(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러오지 못했습니다!");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }


    //게시글 등록
    @ApiOperation(value = "게시판", notes = "게시판에 글을 등록한다.", response = Map.class)
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registBoard(@RequestBody @ApiParam(value = "등록할 게시글 정보", required = true) BoardDto boardDto, Authentication authentication) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();

        String userId = authentication.getName();
        User presentUser = userService.findByUserId(userId);
        boardDto.setUserId(presentUser.getUserId());
        boardDto.setCreateTime(LocalDateTime.now());
        try {
            BoardDto newBoardDto = boardService.registBoard(boardDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 등록 완료!");
            resultMap.put("data", newBoardDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 등록 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 삭제
    @ApiOperation(value = "게시판", notes = "게시판에 글을 삭제한다.", response = Map.class)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("boardId") @ApiParam(value = "삭제할 게시글 번호", required = true) Long boardId, Authentication authentication) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        Board board = boardService.findBoard(boardId);

        //토큰으로 유저를 탐지해서 현재 board의 글 작성자가 맞는지 확인하기
        if (!board.getUser().getUserId().equals(userId)) {
            resultMap.put("code", "401");
            resultMap.put("msg", "삭제 권한이 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
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
    @ApiOperation(value = "게시판", notes = "게시판에 글을 수정한다.", response = Map.class)
    @PutMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> modifyBoard(@PathVariable("boardId") @ApiParam(value = "수정할 게시글 번호", required = true) Long boardId, @RequestBody @ApiParam(value = "수정할 게시글 내용", required = true) BoardDto boardDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        Board board = boardService.findBoard(boardId);

        //토큰으로 유저를 탐지해서 현재 board의 글 작성자가 맞는지 확인하기
        if (!board.getUser().getUserId().equals(userId)) {
            resultMap.put("code", "401");
            resultMap.put("msg", "삭제 권한이 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
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
    @ApiOperation(value = "게시판", notes = "게시판에서 게시글을 눌렀을 때 냬용을 볼 수 있게 해준다.", response = Map.class)
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> boardDetail(@PathVariable("boardId") @ApiParam(value = "자세히 볼 게시글 번호", required = true) Long boardId, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            BoardDto newBoardDto = boardService.boardDetail(boardId);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 불러오기 완료!");
            resultMap.put("data", newBoardDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 불러오기 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 머리말대로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시판", notes = "게시판에서 머리말대로 검색을 할 수 있게 해준다.", response = Map.class)
    @GetMapping("/sort/{sort}")
    public ResponseEntity<Map<String, Object>> boardBySort(@PathVariable("sort") @ApiParam(value = "검색할 머리말", required = true) Sort sort, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Page<BoardDto> pageList = boardService.boardBySort(sort, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 머리말로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 머리말로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 작성자로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시판", notes = "게시판에서 작성자로id 검색을 할 수 있게 해준다.", response = Map.class)
    @GetMapping("/user/{userid}")
    public ResponseEntity<Map<String, Object>> boardByUser(@PathVariable("userid") @ApiParam(value = "검색할 작성자 id", required = true) String userName, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Page<BoardDto> pageList = boardService.boardByUser(userName, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 작성자로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 작성자로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 제목으로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시판", notes = "게시판에서 머리말대로 검색을 할 수 있게 해준다.", response = Map.class)
    @GetMapping("/title/{title}")
    public ResponseEntity<Map<String, Object>> boardByTitle(@PathVariable("title") @ApiParam(value = "검색할 내용", required = true) String title, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Page<BoardDto> pageList = boardService.boardByTitle(title, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 제목으로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 제목으로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 내용으로로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시판", notes = "게시판에서 내용으로 검색을 할 수 있게 해준다.", response = Map.class)
    @GetMapping("/content/{content}")
    public ResponseEntity<Map<String, Object>> BoardByContent(@PathVariable("content") @ApiParam(value = "검색할 내용", required = true) String content, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Page<BoardDto> pageList = boardService.boardByContent(content, pageable);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 내용으로 검색 완료!");
            resultMap.put("data", pageList);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 내용으로 검색 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 작성하기
    @ApiOperation(value = "게시판", notes = "게시판에서 댓글을 작성할 수 있게 해준다.", response = Map.class)
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Map<String, Object>> registComment(@PathVariable("boardId") @ApiParam(value = "작성하고 있는 게시판 id", required = true) Long boardId, @RequestBody @ApiParam(value = "등록할 댓글 내용", required = true) CommentDto commentDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        commentDto.setUserId(userId);
        commentDto.setCreateTime(LocalDateTime.now());
        commentDto.setBoardId(boardId);
        System.out.println(commentDto.getContent());
        try {
            CommentDto newCommentDto = commentService.registComment(commentDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 작성 성공!");
            resultMap.put("data", newCommentDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 작성 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 삭제하기
    @ApiOperation(value = "게시판", notes = "게시판에서 댓글을 삭제한다.", response = Map.class)
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId") @ApiParam(value = "작성하고 있는 댓글 id", required = true) Long commentId, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        //댓글을 작성한 사람과 현재 로그인 한 사람이 다르다면
        if (!commentService.findComment(commentId).getUser().getUserId().equals(userId)) {
            resultMap.put("code", "401");
            resultMap.put("msg", "해당 댓글 작성자만 댓글을 삭제 할 수 있습니다.!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
            try {
                commentService.deleteComment(commentId);
                resultMap.put("code", "200");
                resultMap.put("msg", "해당 게시물에 댓글 삭제 성공!");
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("code", "500");
                resultMap.put("msg", "해당 게시물에 댓글 삭제 실패!");
            }
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }

    //게시글에 댓글 수정하기
    @ApiOperation(value = "게시판", notes = "게시판에서 댓글을 수정한다.", response = Map.class)
    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable("commentId") @ApiParam(value = "현재 댓글 id", required = true) Long commentId, @RequestBody @ApiParam(value = "수정할 댓글 내용", required = true) CommentDto commentDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        commentDto.setCreateTime(LocalDateTime.now());
        //댓글을 작성한 사람과 현재 로그인 한 사람이 다르다면
        if (!commentService.findComment(commentId).getUser().getUserId().equals(userId)) {
            resultMap.put("code", "401");
            resultMap.put("msg", "해당 댓글 작성자만 댓글을 수정 할 수 있습니다.!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
        try {
            CommentDto newCommentDto = commentService.updateComment(commentDto);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 수정 성공!");
            resultMap.put("data", newCommentDto);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 수정 실패!");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
}
