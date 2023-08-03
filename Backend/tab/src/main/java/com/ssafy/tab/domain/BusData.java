package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class BusData {
    /*

    id : 데이터 번호
    busStation : 버스 정류장 정보
    count : 탑승인원
    boardingTime : 탑승 시간
    vehicleNo : 버스 차량 번호
    routeNo : 버스 노선 번호
    vulnerable : 교통약자 여부
     */


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATA_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_NO")
    private BusStation busStation;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "BOARDING_TIME")
    private LocalDateTime boardingTime;

    @Column(name = "VEHICLE_NO")
    private String vehicleNo;

    @Column(name = "ROUTE_NO", length = 20)
    private String routeNo;

    @Column(name = "VULNERABLE")
    private boolean vulnerable;
}
