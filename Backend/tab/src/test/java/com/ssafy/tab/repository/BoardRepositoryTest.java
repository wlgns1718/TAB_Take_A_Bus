package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ssafy.tab.domain.Role.USER;

@SpringBootTest
@Transactional
@Rollback(value = false)
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    //게시글 등록
    @Test
    public void createBoard() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목입니다.", "내용입니다", now, Sort.REPORT);
        boardService.createBoard(board1);
    }
    
    //게시글 삭제
    @Test
    public void deleteBoard() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목입니다.", "내용입니다", now, Sort.REPORT);
        boardService.createBoard(board1);
        
        //생성 된 게시글 삭제
        boardService.deleteBoard(board1);
    }

    //게시글 수정
    @Test
    public void modifyBoard() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목입니다.", "내용입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        BoardDto boardDto = new BoardDto();
        boardDto.setContent("수정내용입니다.");
        boardDto.setTitle("수정 제목입니다.");
        LocalDateTime nowTime = LocalDateTime.now();
        boardDto.setCreateTime(nowTime);

        Board board = boardService.modifyBoard(board1.getId(), boardDto);
        System.out.println("수정된 board의 id = " + board.getId());
    }

    //게시글 자세히보기
    @Test
    public void selectBoard() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목입니다.", "내용입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        Board board = boardService.selectBoard(board1.getId());
        System.out.println("board.getContent() = " + board.getContent());
        System.out.println("board 생성 시간은 = " + board.getCreateTime());
    }
}