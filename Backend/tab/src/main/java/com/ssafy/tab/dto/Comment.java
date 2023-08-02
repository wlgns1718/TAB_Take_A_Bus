package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {

    /*
    boardNo : 게시판 번호 
    content : 내용
    createTime : 작성 날짜
     */

    Long boardNo;
    String content;
    LocalDateTime createTime;
    public Comment() {
    }

    public Comment(Long boardNo, String content, LocalDateTime createTime) {
        this.boardNo = boardNo;
        this.content = content;
        this.createTime = createTime;
    }
}
