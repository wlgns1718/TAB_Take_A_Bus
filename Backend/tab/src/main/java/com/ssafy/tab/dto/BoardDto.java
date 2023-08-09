package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardDto {
    
    /*
    id : 게시글 id
    userId : 게시글 작성자의 id
    userName : 게시글 작성자의 이름
    title : 제목
    content :  내용
    createTime : 작성시간
    sort : 머리말(종류)
    commentDtoList : 댓글 리스트
     */

    Long id;
    Long userId;
    String userName;
    String title;
    String content;
    LocalDateTime createTime;
    Sort sort;
    List<CommentDto> commentDtoList;

    //boardDto에 commentDto를 추가 및 삭제하는 메서드
    //게시물을 자세히 보기 했을 때마 추가.
    public void addComment(CommentDto commentDto) {
        this.commentDtoList.add(commentDto);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public BoardDto(Long id, Long userId, String userName, String title, String content, LocalDateTime createTime, Sort sort) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.sort = sort;
        this.commentDtoList = new ArrayList<>();
    }

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .userName(board.getUser().getName())
                .title(board.getTitle())
                .content(board.getContent())
                .createTime(board.getCreateTime())
                .sort(board.getSort())
                .commentDtoList(new ArrayList<>())
                .build();
    }
}
