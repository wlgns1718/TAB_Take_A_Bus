package com.ssafy.tab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter @Setter
public class CommentDto {

    /*
    id : 댓글 id
    content : 내용
    createTime : 작성 날짜
     */
    Long id;
    String content;
    LocalDateTime createTime;
}
