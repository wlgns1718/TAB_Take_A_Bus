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
    private int count;

    @Column(name = "BOARDING_TIME")
    private LocalDateTime boardingTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_NO")
    private BusStation busStation;

}
