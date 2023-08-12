package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class BusAPI {

    private int remainingStops; // 도착예정버스 남은 정류장 수
    private int eta; // 도착예정버스 도착예상시간
    private String routeId; // 노선ID
    private String busNo; // 버스번호 "급행2"와 같이 숫자와 문자가 섞인 경우 때문에 String
    private String routeType; // 노선유형
    private String vehicleType; // 도착예정버스 차량유형

    private int stationOrder; // 정류소 순서
    private String vehicleNo; // 차량번호
    private String stationId; // 현재 위치 정류소ID
    private String stationName; // 현재 위치 정류소명
}

