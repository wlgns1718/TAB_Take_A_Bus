package com.ssafy.tab.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class BusStation {

    @Id
    @Column(name = "STATION_NO", length = 20)
    private String id; // 버스정류장 번호

    @Column(name = "CITY_NAME", length = 10)
    private String cityName; // 도시이름

    @Column(name = "CITY_CODE")
    private int cityCode; // 도시코드

    @Column(name = "STATION_NAME", length = 30)
    private String stationName; // 정류장명

    @Column(name = "LATITUDE")
    private Double latitude; // 위도

    @Column(name = "LONGTITUDE")
    private Double longtitude; // 경도
}
