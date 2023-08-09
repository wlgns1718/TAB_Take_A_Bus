package com.ssafy.tab.service;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.CommentRequestDto;
import com.ssafy.tab.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    //댓글 id로 댓글 찾아오기, 댓글 작성하기, 댓글 삭제하기, 댓글 수정하기

    // 댓글 id로 댓글 찾아오기
    @Transactional(readOnly = true)
    public Comment findComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        return comment;
    }
    //댓글 작성하기
    public void registComment(CommentRequestDto commentRequestDto, Long boardId, String UserId){
        Board board = boardService.findBoard(boardId);
        User user = userService.findByUserId(UserId);
        commentRepository.save(Comment.toEntity(commentRequestDto, user, board, LocalDateTime.now()));
    }

    //댓글 삭제하기
    public void deleteComment(Long commentid){
        Comment comment = commentRepository.findById(commentid).get();
        commentRepository.delete(comment);
    }

    //댓글 수정하기
    public void updateComment(CommentRequestDto commentRequestDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.changeComment(commentRequestDto, LocalDateTime.now());
    }
}
