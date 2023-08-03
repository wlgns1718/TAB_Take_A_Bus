package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Notice;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDto {
    String title;
    String content;

    public NoticeDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
