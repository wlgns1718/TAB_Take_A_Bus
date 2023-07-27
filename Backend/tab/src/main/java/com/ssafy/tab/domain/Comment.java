package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "COMMENT")
public class Comment {
    
    /*
    id : 댓글 번호
    userNo : 유저 번호 fk
    boardNo : 게시판 번호 fk
    content : 내용
    createTime : 작성날짜
     */

    @Id @GeneratedValue
    @Column(name = "COMMENT_NO")
    private Long id;

    @Column(name = "USER_NO")
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "BOARD_NO")
    @JoinColumn(name = "BOARD_NO")
    private Long boardNo;

    @Column(name = "CONTENT", length = 200)
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

}
