package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Sort;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BoardDto {
    
    /*
    id : 게시글 id
    userId : 게시글 작성자의 id
    userName : 게시글 작성자의 이름
    title : 제목
    content :  내용
    createTime : 작성시간
    sort : 머리말(종류)(
     */

    Long id;
    String userName;
    String title;
    String content;
    LocalDateTime createTime;
    Sort sort;

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", sort=" + sort +
                '}';
    }
}
