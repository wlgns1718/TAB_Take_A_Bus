package com.ssafy.tab.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    /*
    content : 내용
     */
    String content;
}
