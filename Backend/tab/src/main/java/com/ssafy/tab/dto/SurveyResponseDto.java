package com.ssafy.tab.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class SurveyResponseDto {

    double latitude; //위도
    double longtitude; //경도

}
