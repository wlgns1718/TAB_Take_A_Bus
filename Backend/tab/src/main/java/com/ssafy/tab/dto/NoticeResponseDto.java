package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeResponseDto { // 공지사항 응답에 사용되는 DTO
    String userName;
    String title;
    String content;
    LocalDateTime createTime;

    public NoticeResponseDto(String userName, String title, String content, LocalDateTime createTime) {
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }


}
