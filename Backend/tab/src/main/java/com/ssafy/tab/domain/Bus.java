package com.ssafy.tab.domain;


import com.ssafy.tab.dto.BusDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {

    /*
    id : 버스 차량 번호
    routeNo : 버스 노선 번호
    createDate : 날짜
    stationId : 버스정류장 번호
    vulnerable : 교통약자 여부
     */


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUS_ID")
    private Long id;

    @Column(name = "VEHICLE_NO", length = 20)
    private String vehicleNo;


    @Column(name = "ROUTE_NO", length = 20)
    private String routeNo;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "STAION_ID", length = 20)
    private String stationId;

    @Column(name = "VULNERABLE")
    private boolean vulnerable;

    public static Bus toEntity(BusDto busDto){
        return Bus.builder()
                .vehicleNo(busDto.getVehicleNo())
                .routeNo(busDto.getRouteNo())
                .createDate(LocalDateTime.now())
                .vulnerable(busDto.isVulnerable())
                .stationId(busDto.getStationId())
                .build();
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", routeNo='" + routeNo + '\'' +
                ", createDate=" + createDate +
                ", stationId='" + stationId + '\'' +
                ", vulnerable=" + vulnerable +
                '}';
    }
}
