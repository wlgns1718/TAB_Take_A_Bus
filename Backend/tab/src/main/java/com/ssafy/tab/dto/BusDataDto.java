package com.ssafy.tab.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BusDataDto {
        private String vehicleNo;
        private String routeNo;
        private String stationId;
        private int count;
        private boolean vulnerable;

    @Override
    public String toString() {
        return "BusDataDto{" +
                "vehicleNo='" + vehicleNo + '\'' +
                ", routeNo='" + routeNo + '\'' +
                ", staionId='" + stationId + '\'' +
                ", count=" + count +
                ", vulnerable=" + vulnerable +
                '}';
    }
}
