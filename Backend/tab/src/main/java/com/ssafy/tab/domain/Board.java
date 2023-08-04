package com.ssafy.tab.domain;

import com.ssafy.tab.dto.BoardDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    public Board(User user, String title, String content, LocalDateTime createTime, Sort sort) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.sort = sort;
    }

    //게시글의 내용을 수정하는 기능.
    public void changeBoard(BoardDto boardDto) {
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.createTime = boardDto.getCreateTime();
        this.sort = boardDto.getSort();
    }
}
