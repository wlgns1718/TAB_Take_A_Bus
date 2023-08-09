package com.ssafy.tab.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NoticeListResponseDto {
    Long id;
    String userName;
    String title;
    Timestamp createTime;

    public NoticeListResponseDto(Long id, String userName, String title, Timestamp createTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.createTime = createTime;
    }
}
