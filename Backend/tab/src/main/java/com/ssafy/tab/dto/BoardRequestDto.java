package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Sort;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardRequestDto {

   /*
   title : 게시글 제목
   content : 게시글 내용
   sort : 게시글 머리말.
    */

    String title;
    String content;
    Sort sort;
}
