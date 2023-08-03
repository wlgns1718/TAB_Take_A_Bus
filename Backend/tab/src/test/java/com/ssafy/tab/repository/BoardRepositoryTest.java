package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ssafy.tab.domain.Role.USER;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:/application-test.properties")
@Rollback(value = false)
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
}