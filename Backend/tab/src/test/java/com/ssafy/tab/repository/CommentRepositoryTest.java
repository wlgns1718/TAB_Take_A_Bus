package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.CommentDto;
import com.ssafy.tab.service.BoardService;
import com.ssafy.tab.service.CommentService;
import com.ssafy.tab.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ssafy.tab.domain.Role.USER;

@TestPropertySource(locations="classpath:/application-test.properties")
@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;
    //댓글 작성하기
    @Test
    public void createComment() throws Exception{
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목1입니다.", "내용1입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        Board board2 = new Board(user, "제목2입니다.", "내용2입니다", now, Sort.SUGGESTION);
        boardService.createBoard(board2);


        for(int i = 0; i < 10; i++){
            Comment comment = new Comment(user, board1, "댓글" + i + "입니다.",  now);
            commentService.createComment(comment);
        }

        Comment findComment = commentService.findComment(1L);
        System.out.println("찾은 댓글이 작성된 게시글 제목 = " + findComment.getBoard().getTitle());
        System.out.println("찾은 댓글이 작성된 게시글 내용 = " + findComment.getBoard().getContent());
        System.out.println("findComment.getContent() = " + findComment.getContent());
    }
    
    //댓글 삭제하기
    @Test
    public void deleteComment() throws Exception{
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목1입니다.", "내용1입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        Board board2 = new Board(user, "제목2입니다.", "내용2입니다", now, Sort.SUGGESTION);
        boardService.createBoard(board2);


        for(int i = 0; i < 10; i++){
            commentService.createComment(new Comment(user, board1, "댓글" + i + "입니다.",  now));
        }

        Comment comment = new Comment(user, board2, "찾는 댓글입니다.",  now);
        commentService.createComment(comment);
        Comment findComment = commentService.findComment(comment.getId());
        System.out.println("삭제 전 댓글의 내용은 = " + findComment.getContent());
        commentService.deleteComment(findComment);
        Comment findComment2 = commentService.findComment(comment.getId());
        if (findComment2 == null) {
            System.out.println("삭제 되었습니다.");
        }
        else{
            System.out.println("삭제 되지 않았습니다.");
        }
    }

    //댓글 수정하기
    @Test
    public void updateComment() throws Exception{
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목1입니다.", "내용1입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        Board board2 = new Board(user, "제목2입니다.", "내용2입니다", now, Sort.SUGGESTION);
        boardService.createBoard(board2);


        for(int i = 0; i < 10; i++){
            commentService.createComment(new Comment(user, board1, "댓글" + i + "입니다.",  now));
        }

        Comment comment = new Comment(user, board1, "찾는 댓글입니다.",  now);
        commentService.createComment(comment);
        Comment findComment = commentService.findComment(comment.getId());
        System.out.println("찾은 댓글이 작성된 게시글 제목 = " + findComment.getBoard().getTitle());
        System.out.println("찾은 댓글이 작성된 게시글 내용 = " + findComment.getBoard().getContent());
        System.out.println("댓글 내용 = " + findComment.getContent());
        CommentDto commentDto = new CommentDto();
        commentDto.setId(findComment.getId());
        commentDto.setContent("수정 된 댓글입니다.");
        commentDto.setCreateTime(now);
        Comment findComment2 = commentService.updateComment(commentDto);
        System.out.println("찾은 댓글이 작성된 게시글 제목 = " + findComment2.getBoard().getTitle());
        System.out.println("찾은 댓글이 작성된 게시글 내용 = " + findComment2.getBoard().getContent());
        System.out.println("댓글 내용 = " + findComment2.getContent());
    }

    @Test
    public void selectComentBoard() throws Exception{
        User user = new User("qweqwe13", "1234", "홍길동", "qwe@naver.com", USER);
        userService.joinUser(user);

        LocalDateTime now = LocalDateTime.now();

        Board board1 = new Board(user, "제목1입니다.", "내용1입니다", now, Sort.REPORT);
        boardService.createBoard(board1);

        Board board2 = new Board(user, "제목2입니다.", "내용2입니다", now, Sort.SUGGESTION);
        boardService.createBoard(board2);


        for(int i = 0; i < 10; i++){
            commentService.createComment(new Comment(user, board1, "게시글 1의 댓글" + i + "입니다.",  now));
        }

        Comment comment = new Comment(user, board2, "게시글2의 댓글입니다.",  now);
        commentService.createComment(comment);

        List<Comment> comments = commentService.selectComentBoard(board1);
        for (Comment findComment : comments) {
            System.out.println("달린 댓글은 " + findComment.getContent());

        }
    }
}