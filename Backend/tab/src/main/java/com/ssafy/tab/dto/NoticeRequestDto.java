package com.ssafy.tab.dto;

import lombok.Data;

@Data
public class NoticeRequestDto {
    String title;
    String content;

    public NoticeRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
