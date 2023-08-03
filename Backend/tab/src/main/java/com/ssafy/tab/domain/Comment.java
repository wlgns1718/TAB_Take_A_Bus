package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {
    
    /*
    id : 댓글 번호
    userNo : 유저 번호 fk
    boardNo : 게시판 번호 fk
    content : 내용
    createTime : 작성날짜
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOARD_NO")
    private Board boardNo;

    @Column(name = "CONTENT", length = 200)
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

}
