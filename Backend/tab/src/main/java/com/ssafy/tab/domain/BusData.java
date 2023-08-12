package com.ssafy.tab.domain;

import com.ssafy.tab.dto.BusDataDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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


    public static BusData toEntity(BusDataDto busDataDto, BusStation busStation){
        return BusData.builder()
                .busStation(busStation)
                .count(busDataDto.getCount())
                .vulnerable(busDataDto.isVulnerable())
                .vehicleNo(busDataDto.getVehicleNo())
                .routeNo(busDataDto.getRouteNo())
                .boardingTime(LocalDateTime.now())
                .build();
    }
    //버스 정보 등록을 위한 생성자


    @Override
    public String toString() {
        return "BusData{" +
                "id=" + id +
                ", busStation=" + busStation +
                ", count=" + count +
                ", boardingTime=" + boardingTime +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", routeNo='" + routeNo + '\'' +
                ", vulnerable=" + vulnerable +
                '}';
    }
}
