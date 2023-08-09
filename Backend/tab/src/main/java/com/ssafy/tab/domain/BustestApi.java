package com.ssafy.tab.domain;

import lombok.*;

import javax.persistence.Column;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BustestApi {

    private int remainingStops; // 도착예정버스 남은 정류장 수 x
    private int eta; // 도착예정버스 도착예상시간 x
    private String routeId; // 노선ID x
    private String routeNo; // 노선번호
    private String busNo; // 버스번호 "급행2"와 같이 숫자와 문자가 섞인 경우 때문에 String x
    private String routeType; // 노선유형 x
    private String vehicleType; // 도착예정버스 차량유형 x


    private int stationOrder; // 정류소 순서 x
    private String vehicleNo; // 차량번호
    private String stationId; // 현재 위치 정류소ID x
    private String stationName; // 현재 위치 정류소명 x
    private Double latitude; // 현재 버스가 출발한 정류장의 위도 x
    private Double longtitude; //현재 버스가 출바한 정류장의 경도 x

    @Override
    public String toString() {
        return "BustestApi{" +
                "remainingStops=" + remainingStops +
                ", eta=" + eta +
                ", routeId='" + routeId + '\'' +
                ", busNo='" + busNo + '\'' +
                ", routeType='" + routeType + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", stationOrder=" + stationOrder +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}

