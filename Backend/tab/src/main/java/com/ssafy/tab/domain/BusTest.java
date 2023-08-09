package com.ssafy.tab.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor()
@Getter @Setter
@Table(name = "BUS_TEST")
public class BusTest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "ROUTE_NO")
    String routeNo;
    @Column(name = "ROUTE_ID")
    String routeId;
    @Column(name = "STATION_ID")
    String stationId;
    @Column(name = "LATITUDE")
    Double latitude;
    @Column(name = "LONGTITUDE")
    Double longtitude;
    @Column(name = "STATION_NAME")
    String stationName;
    @Column(name = "ORDER_STOP")
    int orderStop;
}
