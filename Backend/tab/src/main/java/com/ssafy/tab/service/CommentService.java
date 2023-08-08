package com.ssafy.tab.service;


import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.CommentDto;
import com.ssafy.tab.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    //댓글 작성하기, 댓글 삭제하기, 댓글 수정하기

    //댓글 작성하기
    public CommentDto registComment(CommentDto commentDto){
        Board board = boardService.findBoard(commentDto.getBoardId());
        User user = userService.findById(commentDto.getUserId()).get();
        return CommentDto.toDto(commentRepository.save(Comment.toEntity(commentDto, user, board)));
    }

    //댓글 삭제하기
    public void deleteComment(Long commentid){
        Comment comment = commentRepository.findById(commentid).get();
        commentRepository.delete(comment);
    }

    //댓글 수정하기
    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).get();
        comment.changeComment(commentDto);
        return CommentDto.toDto(comment);
    }
}
