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
    @Column(name = "STATION_NO")
    private String id;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "CITY_CODE")
    private int cityCode;

    @Column(name = "STATION_NAME")
    private String stationName;

    @Column(name = "LATITUDE")
    private Float latitude;

    @Column(name = "LONGTITUDE")
    private Float longtitude;

    @OneToMany(mappedBy = "busStation",cascade = CascadeType.ALL)
    List<BusStationData> data = new ArrayList<>();
}
