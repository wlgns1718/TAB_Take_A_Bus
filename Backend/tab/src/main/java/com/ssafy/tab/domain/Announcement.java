package com.ssafy.tab.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "COMMENT")
public class Announcement {

    @Id @GeneratedValue
    @Column(name = "ANNOUNCEMENT_NO")
    private Long id;

    @Column(name = "USER_NO")
    private User user;

    @Column(name = "TITLE", columnDefinition="TEXT")
    private String title;

    @Column(name = "CONTENT", columnDefinition="TEXT")
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;


}
