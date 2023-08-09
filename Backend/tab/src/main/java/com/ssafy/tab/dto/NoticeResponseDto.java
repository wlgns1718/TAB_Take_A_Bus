package com.ssafy.tab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Getter @Setter
public class NoticeResponseDto { // 공지사항 응답에 사용되는 DTO
    Long id;
    String userName;
    String title;
    String content;
    Timestamp createTime;

    public NoticeResponseDto(Long id,String userName, String title, String content, Timestamp createTime) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }


}
