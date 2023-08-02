package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDto {
    
    /*
    title : 제목
    content :  내용
    createTime : 작성시간
     */
    
    String title;
    String content;
    LocalDateTime createTime;

    public BoardDto() {}

    public BoardDto(String title, String content, LocalDateTime createTime) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }
}
