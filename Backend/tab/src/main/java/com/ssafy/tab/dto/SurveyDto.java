package com.ssafy.tab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter @Setter
public class SurveyDto {

    /*
    startLatitude : 출발지 위도
    startLongtitude : 출발지 경도
    destinationLatitude : 목적지 위도
    destinationLongtitude : 목적지 경도
     */
    LocalDateTime createDate;
    double startLatitude;
    double startLongtitude;
    double destinationLatitude;
    double destinationLongtitude;

    public SurveyDto(LocalDateTime createDate, double startLatitude, double startLongtitude, double destinationLatitude, double destinationLongtitude) {
        this.createDate = createDate;
        this.startLatitude = startLatitude;
        this.startLongtitude = startLongtitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongtitude = destinationLongtitude;
    }
}
