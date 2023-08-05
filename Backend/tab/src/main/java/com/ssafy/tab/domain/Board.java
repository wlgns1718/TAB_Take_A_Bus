package com.ssafy.tab.domain;

import com.ssafy.tab.dto.BoardDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "BOARD")
public class Board {

    /*
    id : 게시판 번호
    userNo : 글 작성자 id
    title : 제목
    content : 내용
    createTime : 작성 시간
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "TITLE", columnDefinition="TEXT")
    private String title;

    @Column(name = "CONTENT", columnDefinition="TEXT")
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "SORT")
    private Sort sort;

    //게시글의 내용을 수정하는 기능.
    public Board changeBoard(BoardDto boardDto) {
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.createTime = boardDto.getCreateTime();
        this.sort = boardDto.getSort();
        return this;
    }

    public static Board toEntity(BoardDto boardDto, User user) {
        return Board.builder()
                .id(boardDto.getId())
                .user(user)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .createTime(boardDto.getCreateTime())
                .sort(boardDto.getSort())
                .build();
    }
}
