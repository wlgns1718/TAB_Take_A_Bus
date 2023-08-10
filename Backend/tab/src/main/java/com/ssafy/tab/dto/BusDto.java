package com.ssafy.tab.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class BusDto {

    private String id;
    private String routeNo;
    private LocalDateTime createDate;
    private String stationId;
    private boolean vulnerable;

    public BusDto(String id, boolean vulnerable){
        this.id = id;
        this.vulnerable = vulnerable;
    }
}
