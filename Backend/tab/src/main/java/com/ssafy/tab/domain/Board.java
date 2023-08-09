package com.ssafy.tab.domain;

import com.ssafy.tab.dto.BoardRequestDto;
import com.ssafy.tab.dto.BoardResponseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    public Board(User user, String title, String content, LocalDateTime dateTime, Sort sort) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createTime = dateTime;
        this.sort = sort;
    }

    //게시글의 내용을 수정하는 기능.
    public void changeBoard(BoardRequestDto boardRequestDto, LocalDateTime dateTime) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.sort = boardRequestDto.getSort();
        this.createTime = dateTime;
    }

    public static Board toEntity(BoardResponseDto boardResponseDto, User user) {
        return Board.builder()
                .id(boardResponseDto.getId())
                .user(user)
                .title(boardResponseDto.getTitle())
                .content(boardResponseDto.getContent())
                .createTime(boardResponseDto.getCreateTime())
                .sort(boardResponseDto.getSort())
                .build();
    }
}
