package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.dto.BoardResponseDto;
import com.ssafy.tab.dto.BoardRequestDto;
import com.ssafy.tab.dto.CommentRequestDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tab/board")
@Api("게시판 컨트롤러 API")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    //전체 게시글
    @ApiOperation(value = "게시판 목록", notes = "게시판 전체 목록을 보여줍니다.(페이징 처리 필수)", response = Map.class)
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> board(Pageable pageable, Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Page<BoardResponseDto> list = boardService.list(pageable);
            resultMap.put("data", list);
            resultMap.put("code", "200");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러오지 못했습니다!");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //게시글 등록
    @ApiOperation(value = "게시글 등록", notes = "게시글을 작성할 수 있습니다", response = Map.class)
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registBoard(@RequestBody @ApiParam(value = "게시판 작성에 필요한 요소", required = true) BoardRequestDto boardRequestDto , Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try{
            boardService.createBoard(boardRequestDto, userId);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 등록 완료!");
        }catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 등록 실패!");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //게시글 자세히보기
    @ApiOperation(value = "게시글 클릭 시 전체 내용 보기", notes = "게시글을 클릭 시 전체 내용을 볼 수 있습니다.", response = Map.class)
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> boardDetail(@PathVariable("boardId") @ApiParam(value = "게시글의 id", required = true) Long boardId, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            BoardResponseDto boardResponseDto = boardService.boardDetail(boardId);
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 불러오기 완료!");
            resultMap.put("data", boardResponseDto);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 불러오기 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "해당 게시글을 작성한 작성자는 삭제를 할 수 있습니다.", response = Map.class)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("boardId") @ApiParam(value = "게시글의 id", required = true) Long boardId, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        if(!boardService.findBoard(boardId).getUser().getUserId().equals(userId)){
            resultMap.put("code", "401");
            resultMap.put("msg", "다른 사람이 작성한 글은 삭제 할 수 없습니다.");
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
    @PutMapping("/{boardId}")
    @ApiOperation(value = "게시글 수정", notes = "해당 게시글을 작성한 작성자는 수정을 할 수 있습니다.", response = Map.class)
    public ResponseEntity<Map<String, Object>> modifyBoard(@PathVariable("boardId") @ApiParam(value = "게시글의 id", required = true)Long boardId, @RequestBody @ApiParam(value = "게시글 수정에 필요한 요소", required = true)BoardRequestDto boardRequestDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        if(!boardService.findBoard(boardId).getUser().getUserId().equals(userId)){
            resultMap.put("code", "401");
            resultMap.put("msg", "다른 사람이 작성한 글은 수정 할 수 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
        try {
            boardService.modifyBoard(boardId, boardRequestDto);
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 수정 완료!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 수정 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }


    //게시글 머리말대로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시글 머리말로 검색", notes = "게시글의 머리말을 검색할 수 있습니다. REPORT, COMPLAIN, COMPLIMENT, SUGGESTION만 됩니다.", response = Map.class)
    @GetMapping("/sort/{sort}")
    public ResponseEntity<Map<String, Object>> boardBySort(@PathVariable("sort") @ApiParam(value = "머리말", required = true) Sort sort, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Page<BoardResponseDto> pageList = boardService.boardBySort(sort, pageable);
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


    //게시글 제목으로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시글의 제목으로 검색", notes = "게시글의 제목으로 검색할 수 있습니다. 이 때 검색어가 부분만 포함된 내용도 출력됩니다.", response = Map.class)
    @GetMapping("/title/{title}")
    public ResponseEntity<Map<String, Object>> boardByTitle(@PathVariable("title") @ApiParam(value = "제목", required = true) String title, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Page<BoardResponseDto> pageList = boardService.boardByTitle(title, pageable);
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


    //게시글 작성자로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시글의 작성자로 검색 ", notes = "게시글의 작성자(아이디)를 검색할 수 있습니다. 이 때 검색어가 부분만 포함된 내용도 출력됩니다.", response = Map.class)
    @GetMapping("/user/{userName}")
    public ResponseEntity<Map<String, Object>> boardByUser(@PathVariable("userName") @ApiParam(value = "작성자(아이디)", required = true) String userName, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Page<BoardResponseDto> pageList = boardService.boardByUser(userName, pageable);
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

    //게시글 내용으로 검색하기 + 페이징 처리
    @ApiOperation(value = "게시글의 내용으로 검색 ", notes = "게시글의 내용으로 검색할 수 있습니다. 이 때 검색어가 부분만 포함된 내용도 출력됩니다.", response = Map.class)
    @GetMapping("/content/{content}")
    public ResponseEntity<Map<String, Object>> BoardByContent(@PathVariable("content") @ApiParam(value = "내용", required = true) String content, Pageable pageable, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            Page<BoardResponseDto> pageList = boardService.boardByContent(content, pageable);
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
    @ApiOperation(value = "게시글에 댓글 작성", notes = "게시글에 댓글을 작성 할 수 있습니다.", response = Map.class)
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Map<String, Object>> registComment(@PathVariable("boardId")@ApiParam(value = "게시글 id", required = true) Long boardId, @RequestBody @ApiParam(value = "댓글에 필요한 요소", required = true) CommentRequestDto commentRequestDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try{
            commentService.registComment(commentRequestDto, boardId, userId);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 작성 성공!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 작성 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글에 댓글 삭제하기
    @ApiOperation(value = "게시글에 댓글 삭제", notes = "자신이 작성한 댓글을 삭제 할 수 있습니다.", response = Map.class)
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId") @ApiParam(value = "댓글 id", required = true) Long commentId, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        if(!commentService.findComment(commentId).getUser().getUserId().equals(userId)){
            resultMap.put("code", "401");
            resultMap.put("msg", "다른 사람이 작성한 댓글은 삭제 할 수 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
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
    @ApiOperation(value = "게시글에 댓글 삭제", notes = "자신이 작성한 댓글을 수정 할 수 있습니다.", response = Map.class)
    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable("commentId") @ApiParam(value = "댓글 id", required = true) Long commentId,  @ApiParam(value = "수정할 댓글 요소", required = true)@RequestBody CommentRequestDto commentRequestDto, Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        if(!commentService.findComment(commentId).getUser().getUserId().equals(userId)){
            resultMap.put("code", "401");
            resultMap.put("msg", "다른 사람이 작성한 댓글은 수정 할 수 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
        try{
            commentService.updateComment(commentRequestDto, commentId);
            resultMap.put("code", "200");
            resultMap.put("msg", "해당 게시물에 댓글 수정 성공!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "해당 게시물에 댓글 수정 실패!");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }
}