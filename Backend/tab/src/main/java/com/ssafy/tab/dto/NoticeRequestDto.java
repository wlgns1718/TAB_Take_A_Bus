package com.ssafy.tab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter @Setter
public class NoticeRequestDto { // 공지사항 요청에 사용되는 DTO
    String title;
    String content;

    public NoticeRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
