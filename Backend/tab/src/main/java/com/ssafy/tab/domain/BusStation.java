package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class BusStation {

    @Id
    @Column(name = "STATION_NO", length = 20)
    private String id; // 버스정류장 번호

    @Column(name = "CITY_NAME", length = 10)
    private String cityName; // 도시이름

    @Column(name = "CITY_CODE")
    private int cityCode; // 도시코드

    @Column(name = "STATION_NAME", length = 10)
    private String stationName; // 정류장 명

    @Column(name = "LATITUDE")
    private Float latitude; // 위도

    @Column(name = "LONGTITUDE")
    private Float longtitude; // 경도

    @OneToMany(mappedBy = "busStation",cascade = CascadeType.ALL)
    List<BusStationData> data = new ArrayList<>();
}
