package com.ssafy.tab.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "COMMENT")
public class Announcement {

    /*
    id: 공지사항 글 번호
    user: 작성자 NO
    title: 공지사항 제목
    content: 공지사항 내용
    createTime: 공지사항 작성시간
     */

    @Id @GeneratedValue
    @Column(name = "ANNOUNCEMENT_NO")
    private Long id;

    @Column(name = "USER_NO")
    @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "TITLE", columnDefinition="TEXT")
    private String title;

    @Column(name = "CONTENT", columnDefinition="TEXT")
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;


}
