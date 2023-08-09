package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //댓글 작성하기, 댓글 삭제하기, 댓글 수정하기, 게시글 별 댓글 가져오기
    //게시글 별 댓글 가져오기
    List<Comment> findByBoard(Board board);

}
