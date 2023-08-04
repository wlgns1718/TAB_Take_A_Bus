package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeResponseDto { // 공지사항 응답에 사용되는 DTO
    Long id;
    String userName;
    String title;
    String content;
    LocalDateTime createTime;

    public NoticeResponseDto(Long id,String userName, String title, String content, LocalDateTime createTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }


}
