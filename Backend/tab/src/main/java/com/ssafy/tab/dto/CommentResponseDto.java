package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    /*
    id : 댓글 id
    userId : 유저 id
    boardId : 게시글 id
    content : 내용
    createTime : 작성 날짜
     */

    Long id;
    String userId;
    Long boardId;
    String content;
    LocalDateTime createTime;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getUserId())
                .boardId(comment.getBoard().getId())
                .content(comment.getContent())
                .createTime(comment.getCreateTime())
                .build();
    }
}
