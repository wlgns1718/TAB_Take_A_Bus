package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Sort;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDto {
    
    /*
    title : 제목
    content :  내용
    createTime : 작성시간
    sort : 머리말(종류)(
     */
    
    String title;
    String content;
    LocalDateTime createTime;
    Sort sort;

    public BoardDto() {}

    public BoardDto(String title, String content, LocalDateTime createTime, Sort sort) {
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.sort = sort;
    }
}
