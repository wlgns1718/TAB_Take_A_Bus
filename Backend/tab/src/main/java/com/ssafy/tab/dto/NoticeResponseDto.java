package com.ssafy.tab.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeResponseDto {
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
