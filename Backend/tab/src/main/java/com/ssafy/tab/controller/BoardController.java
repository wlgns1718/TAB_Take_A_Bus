package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    //게시판에 접속 시 전체 게시글 가져오기
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> slectBoard(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(request.getHeader("TOKEN"));
        try{
            resultMap.put("code", "500");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러왔습니다.");
            resultMap.put("data", boardService.selectBoard(pageable));
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "200");
            resultMap.put("msg", "정상적으로 게시판 정보를 불러오지 못했습니다!");
        }finally {
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 등록
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createBoard(@RequestBody BoardDto boardDto ,HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(request.getHeader("TOKEN"));
        LocalDateTime localDateTime = LocalDateTime.now();
        //유저를 담기
        User user = userService.findById(2L).get();
        try{
            Board board = new Board(user, boardDto.getTitle(), boardDto.getContent(), localDateTime, boardDto.getSort());
            System.out.println(boardService.createBoard(board));
            resultMap.put("code", "200");
            resultMap.put("msg", "게시글 등록 완료!");
        } catch(Exception e){
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("msg", "게시글 등록 실패!");
        }finally {
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathParam("boardId")Long boardId, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(request.getHeader("TOKEN"));
        //유저를 탐지해서 본인이 작성한 글인지 확인하기.
        User user = new User();
        userService.joinUser(user);
        Board findBoard = boardService.findBoard(boardId);
        if(findBoard.getUser().getId() != user.getId()){
            resultMap.put("code", "401");
            resultMap.put("msg", "삭제 권한이 없습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
        else{
            try{
                boardService.deleteBoard(findBoard);
                resultMap.put("code", "500");
                resultMap.put("msg", "게시글 삭제 완료!");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
            }catch(Exception e){
                e.printStackTrace();
                resultMap.put("code", "200");
                resultMap.put("msg", "게시글 등록 실패!");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
            }
        }
    }

//    //게시글 id로 게시글 엔티티 불러오기
//    @PostMapping("/board/{boardId}")
//    public ResponseEntity<Map<String, Object>> insertBoard(@RequestBody BoardDto boardDto, @PathVariable("boardId") String boardId, HttpServletRequest requset) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = null;
//        System.out.println(requset.getHeader("TOKEN"));
//        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
//    }

}
