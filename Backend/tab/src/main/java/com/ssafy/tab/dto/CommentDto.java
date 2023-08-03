package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    /*
    id : 댓글 id
    content : 내용
    createTime : 작성 날짜
     */

    Long id;
    String content;
    LocalDateTime createTime;
    public CommentDto() {}
}
