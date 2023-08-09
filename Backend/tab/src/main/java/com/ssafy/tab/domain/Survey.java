package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "SURVEY")
public class Survey {

    /*
    id : primary key
    user : user
    createDate : 설문 날짜
    startLatitude : 시작지점 위도
    startLontitude : 시작지점 경도
    destinationLatitude : 목적지 위도
    destinationLongtitude : 목적지 경도
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SURVEY_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_NO")
    private User user;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "START_LATITUDE")
    private double startLatitude;

    @Column(name = "START_LONGTITUDE")
    private double startLontitude;

    @Column(name = "DESTINATION_LATITUDE")
    private double destinationLatitude;

    @Column(name = "DESTINATION_LONGTITUDE")
    private double destinationLongtitude;

    public Survey(User user, LocalDateTime createDate, double startLatitude, double startLontitude, double destinationLatitude, double destinationLongtitude) {
        this.user = user;
        this.createDate = createDate;
        this.startLatitude = startLatitude;
        this.startLontitude = startLontitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongtitude = destinationLongtitude;
    }
}

