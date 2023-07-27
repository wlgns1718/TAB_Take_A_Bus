package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class BusStationData {

    @Id @GeneratedValue
    @Column(name = "DATA_NO")
    private Long id;

    @Column(name = "COUNT")
    private int count; // 탑승인원

    @Column(name = "BOARDING_TIME")
    private LocalDateTime boardingTime; // 탑승시간


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_NO")
    private BusStation busStation;

}
