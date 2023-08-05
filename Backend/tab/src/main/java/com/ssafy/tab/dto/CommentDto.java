package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentDto {

    /*
    id : 댓글 id
    userId : 유저 id
    boardId : 게시글 id
    content : 내용
    createTime : 작성 날짜
     */

    Long id;
    Long userId;
    Long boardId;
    String content;
    LocalDateTime createTime;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .boardId(comment.getBoard().getId())
                .content(comment.getContent())
                .createTime(comment.getCreateTime())
                .build();
    }
}
