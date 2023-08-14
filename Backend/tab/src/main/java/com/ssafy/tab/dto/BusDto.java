package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Bus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusDto {

    private Long id;
    private String vehicleNo;
    private String routeNo;
    private LocalDateTime createDate;
    private String stationId;
    private boolean vulnerable;

    public BusDto(String vehicleNo, boolean vulnerable){
        this.vehicleNo = vehicleNo;
        this.vulnerable = vulnerable;
    }

    public static BusDto toEntity(BusDataDto busDataDto){
        return BusDto.builder()
                .vehicleNo(busDataDto.getVehicleNo())
                .createDate(LocalDateTime.now())
                .routeNo(busDataDto.getRouteNo())
                .stationId(busDataDto.getStationId())
                .vulnerable(busDataDto.isVulnerable())
                .build();
    }

    public static BusDto toEntity(Bus bus){
        return BusDto.builder()
                .vulnerable(bus.isVulnerable())
                .stationId(bus.getStationId())
                .routeNo(bus.getRouteNo())
                .createDate(bus.getCreateDate())
                .vehicleNo(bus.getVehicleNo())
                .id(bus.getId())
                .build();
    }

    @Override
    public String toString() {
        return "BusDto{" +
                "id=" + id +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", routeNo='" + routeNo + '\'' +
                ", createDate=" + createDate +
                ", stationId='" + stationId + '\'' +
                ", vulnerable=" + vulnerable +
                '}';
    }
}
