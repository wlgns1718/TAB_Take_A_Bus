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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.tab.domain.Role.USER;


@TestPropertySource(locations="classpath:/application-test.properties")
@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    //게시글 등록
    @Test
    public void createBoard2() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목입니다.", "내용입니다", now, Sort.REPORT);
        boardService.createBoard(board1);
        System.out.println("board1.getId() = " + board1.getId());
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
        System.out.println("board1.getId() = " + board1.getId());
        
        //생성 된 게시글 삭제
        boardService.deleteBoard(board1);
        System.out.println("board1.getId() = " + board1.getId());

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
        boardDto.setId(board1.getId());
        boardDto.setContent("수정내용입니다.");
        boardDto.setTitle("수정 제목입니다.");
        LocalDateTime nowTime = LocalDateTime.now();
        boardDto.setCreateTime(nowTime);

        Board board = boardService.modifyBoard(boardDto);
        System.out.println("수정된 board의 id = " + board.getId());
        System.out.println("board = " + board.getContent());
        System.out.println("board = " + board.getTitle());
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

        Board board = boardService.findBoard(board1.getId());
        System.out.println("board.getContent() = " + board.getContent());
        System.out.println("board 생성 시간은 = " + board.getCreateTime());
    }

    @Test
    public void selectBoardSort() throws Exception{
        //유저 생성 및 게시글 등록
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 10; i++) {
            boardService.createBoard(new Board(user, "제목"+i+"입니다.", "내용"+i+"입니다", now, Sort.REPORT));
        }

        for (int i = 0; i < 13; i++) {
            boardService.createBoard(new Board(user, "불만제목"+i+"입니다.", "불만내용"+i+"입니다", now, Sort.COMPLAIN));
        }

        PageRequest pageable = PageRequest.of(2, 5);

        Page<Board> boards = boardService.selectBoardSort(Sort.COMPLAIN, pageable);
        List<Board> content = boards.getContent();
        System.out.println("boards.getTotalPages() = " + boards.getTotalPages());
        System.out.println("content.size() = " + content.size());
        for (Board board : content) {
            System.out.println("board의 제목 = " + board.getTitle());
        }
    }

    @Test
    public void selectBoardUser() throws Exception{
        //유저 생성 및 게시글 등록
        User user1 = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        User user2 = new User("태조", "25152", "이성계", "zxcas@naver.com", USER);
        User user3 = new User("정조", "231322", "이산", "ghmhghmg@naver.com", USER);
        userService.joinUser(user1);
        userService.joinUser(user2);
        userService.joinUser(user3);

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 10; i++) {
            boardService.createBoard(new Board(user1, "일반인이 작성한 제목"+i+"입니다.", "일반인이 작성한 내용"+i+"입니다", now, Sort.REPORT));
        }
        for (int i = 0; i < 13; i++) {
            boardService.createBoard(new Board(user2, "태조가 작성한 제목"+i+"입니다.", "태조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }
        for (int i = 0; i < 5; i++) {
            boardService.createBoard(new Board(user3, "정조가 작성한 제목"+i+"입니다.", "장조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }

        PageRequest pageable = PageRequest.of(0, 100);

        Page<Board> boards = boardService.selectBoardUser("태조", pageable);
        List<Board> content = boards.getContent();
        System.out.println("boards.getTotalPages() = " + boards.getTotalPages());
        System.out.println("content.size() = " + content.size());
        for (Board board : content) {
            System.out.println("board의 제목 = " + board.getTitle());
            System.out.println("board를 작성한 사람의 아이디 = " + board.getUser().getUserId());
        }
    }

    @Test
    public void selectBoardTitle() throws Exception{
        //유저 생성 및 게시글 등록
        User user1 = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        User user2 = new User("태조", "25152", "이성계", "zxcas@naver.com", USER);
        User user3 = new User("정조", "231322", "이산", "ghmhghmg@naver.com", USER);
        userService.joinUser(user1);
        userService.joinUser(user2);
        userService.joinUser(user3);

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 10; i++) {
            boardService.createBoard(new Board(user1, "일반인이 작성한 제목"+i+"입니다.", "일반인이 작성한 내용"+i+"입니다", now, Sort.REPORT));
        }
        for (int i = 0; i < 13; i++) {
            boardService.createBoard(new Board(user2, "태조가 작성한 제목"+i+"입니다.", "태조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }
        for (int i = 0; i < 5; i++) {
            boardService.createBoard(new Board(user3, "정조가 작성한 제목"+i+"입니다.", "장조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }

        PageRequest pageable = PageRequest.of(0, 100);

        Page<Board> boards = boardService.selectBoardTitle("일", pageable);
        List<Board> content = boards.getContent();
        System.out.println("boards.getTotalPages() = " + boards.getTotalPages());
        System.out.println("content.size() = " + content.size());
        for (Board board : content) {
            System.out.println("board의 제목 = " + board.getTitle());
            System.out.println("board를 작성한 사람의 아이디 = " + board.getUser().getUserId());
        }
    }

    @Test
    public void selectBoardContent() throws Exception{
        //유저 생성 및 게시글 등록
        User user1 = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        User user2 = new User("태조", "25152", "이성계", "zxcas@naver.com", USER);
        User user3 = new User("정조", "231322", "이산", "ghmhghmg@naver.com", USER);
        userService.joinUser(user1);
        userService.joinUser(user2);
        userService.joinUser(user3);

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 10; i++) {
            boardService.createBoard(new Board(user1, "일반인이 작성한 제목"+i+"입니다.", "일반인이 작성한 내용"+i+"입니다", now, Sort.REPORT));
        }
        for (int i = 0; i < 13; i++) {
            boardService.createBoard(new Board(user2, "태조가 작성한 제목"+i+"입니다.", "태조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }
        for (int i = 0; i < 5; i++) {
            boardService.createBoard(new Board(user3, "정조가 작성한 제목"+i+"입니다.", "장조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }

        PageRequest pageable = PageRequest.of(0, 100);

        Page<Board> boards = boardService.selectBoardContent("장", pageable);
        List<Board> content = boards.getContent();
        System.out.println("boards.getTotalPages() = " + boards.getTotalPages());
        System.out.println("content.size() = " + content.size());
        for (Board board : content) {
            System.out.println("board의 제목 = " + board.getTitle());
            System.out.println("board를 작성한 사람의 아이디 = " + board.getUser().getUserId());
        }
    }

    @Test
    public void createBoard() throws Exception{
        //유저 생성 및 게시글 등록
        User user1 = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        User user2 = new User("태조", "25152", "이성계", "zxcas@naver.com", USER);
        User user3 = new User("정조", "231322", "이산", "ghmhghmg@naver.com", USER);
        userService.joinUser(user1);
        userService.joinUser(user2);
        userService.joinUser(user3);

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 10; i++) {
            boardService.createBoard(new Board(user1, "일반인이 작성한 제목"+i+"입니다.", "일반인이 작성한 내용"+i+"입니다", now, Sort.REPORT));
        }
        for (int i = 0; i < 13; i++) {
            boardService.createBoard(new Board(user2, "태조가 작성한 제목"+i+"입니다.", "태조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }
        for (int i = 0; i < 5; i++) {
            boardService.createBoard(new Board(user3, "정조가 작성한 제목"+i+"입니다.", "장조가 작성한 내용"+i+"입니다", now, Sort.COMPLAIN));
        }

        PageRequest pageable = PageRequest.of(0, 100);

        Page<BoardDto> page = boardService.selectBoard(pageable);
        List<BoardDto> content = page.getContent();
        for (BoardDto board : content) {
            System.out.println(board);
        }
    }
}