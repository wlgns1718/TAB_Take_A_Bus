package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "SURVEY")
public class Survey {

    /*
    id : primary key
    userNo : user테이블 key
    createDate : 설문 날짜
    startLatitude : 시작지점 위도
    startLontitude : 시작지점 경도
    destinationLatitude : 목적지 위도
    destinationLongtitude : 목적지 경도
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SURVEY_NO")
    private Long id;

    @ManyToOne @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "START_LATITUDE")
    private float startLatitude;

    @Column(name = "START_LONGTITUDE")
    private float startLontitude;

    @Column(name = "DESTINATION_LATITUDE")
    private float destinationLatitude;

    @Column(name = "DESTINATION_LONGTITUDE")
    private float destinationLongtitude;

}

