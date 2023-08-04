package com.ssafy.tab.service;


import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import com.ssafy.tab.dto.CommentDto;
import com.ssafy.tab.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    //댓글 id로 댓글 엔티티 불러오기, 댓글 작성하기, 댓글 삭제하기, 댓글 수정하기, 게시글 별 댓글 가져오기

    //댓글 id로 댓글 엔티티 불러오기
    public Comment findComment(Long id) {
        Comment comment = null;
        try{
            comment = commentRepository.findById(id).get();
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return comment;
    }

    //댓글 작성하기
    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }

    //댓글 삭제하기
    public void deleteComment(Comment comment){
        commentRepository.delete(comment);
    }

    //댓글 수정하기
    public Comment updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).get();
        comment.changeComment(commentDto);
        return comment;
    }

    //게시글 별 댓글 가져오기
    @Transactional(readOnly = true)
    public List<Comment> selectComentBoard(Board board) {
        return commentRepository.findByBoard(board);
    }
}
