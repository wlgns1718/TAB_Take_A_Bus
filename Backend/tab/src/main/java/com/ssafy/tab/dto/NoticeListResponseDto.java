package com.ssafy.tab.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class NoticeListResponseDto {
    Long id;
    String userName;
    String title;
    LocalDateTime createTime;

    public NoticeListResponseDto(Long id, String userName, String title, LocalDateTime createTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.createTime = createTime;
    }
}
