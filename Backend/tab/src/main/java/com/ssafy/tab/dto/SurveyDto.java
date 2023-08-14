package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Survey;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@Getter @Setter
public class SurveyDto {

    /*
    startLatitude : 출발지 위도
    startLongtitude : 출발지 경도
    destinationLatitude : 목적지 위도
    destinationLongtitude : 목적지 경도
     */
    double startLatitude;
    double startLongtitude;
    double destinationLatitude;
    double destinationLongtitude;

    public SurveyDto(double startLatitude, double startLongtitude, double destinationLatitude, double destinationLongtitude) {
        this.startLatitude = startLatitude;
        this.startLongtitude = startLongtitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongtitude = destinationLongtitude;
    }

    public static SurveyDto toDto(Survey survey){
        return SurveyDto.builder()
                .startLatitude(survey.getStartLatitude())
                .startLongtitude(survey.getStartLontitude())
                .destinationLatitude(survey.getDestinationLatitude())
                .destinationLongtitude(survey.getDestinationLongtitude())
                .build();

    }
}
