package com.ssafy.tab.service;
import com.ssafy.tab.domain.Survey;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.SurveyDto;
import com.ssafy.tab.dto.SurveyResponseDto;
import com.ssafy.tab.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final UserService userService;
    private final SurveyRepository surveyRepository;
    @Value("${naver.client.id.key}")
    private String clientId;

    @Value("${naver.client.secret.key}")
    private String clientSecret;

    //사용자의 수요 등록
    public void createSurvey(SurveyDto surveyDto, String userId) {
        User user = userService.findByUserId(userId);
        Survey survey = new Survey(user, surveyDto.getStartLatitude(), surveyDto.getStartLongtitude(), surveyDto.getDestinationLatitude(), surveyDto.getDestinationLongtitude());
        surveyRepository.save(survey);
    }

    //내 수요조사 삭제하기
    public void deleteSurvey(String userId) {
        surveyRepository.delete(surveyRepository.findByUser(userService.findByUserId(userId)).get());
    }

    //내가 작성한 수요조사만 가져오기.
    @Transactional(readOnly = true)
    public SurveyDto selectMySurvey(String userId) throws Exception {
        Survey survey = surveyRepository.findByUser(userService.findByUserId(userId)).orElse(null);
        if (survey != null) {
            SurveyDto surveyDto = new SurveyDto(survey.getStartLatitude(), survey.getStartLontitude(), survey.getDestinationLatitude(), survey.getDestinationLongtitude());
            return surveyDto;
        } else {
            throw new Exception();
        }
    }

    //모든 수요조사를 가져오기
    @Transactional(readOnly = true)
    public List<SurveyDto> selectAllSurvey() {
        List<Survey> SurveyList = surveyRepository.findAll();
        List<SurveyDto> result = new ArrayList<>();
        for (Survey survey : SurveyList) {
            result.add(SurveyDto.toDto(survey));
        }
        return result;
    }

    //네이버 api를 사용해서 경도와 위도 뿌려주기.

    public List<SurveyResponseDto> findBestRoute() throws Exception {
        List<Survey> listSurvey = surveyRepository.findAll();
        List<SurveyResponseDto> result = new ArrayList<>();
        for (Survey survey : listSurvey) {
            String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=" + survey.getStartLontitude() + "," + survey.getStartLatitude() + "&goal=" + survey.getDestinationLongtitude() + "," + survey.getDestinationLatitude() + "&option=trafast";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = conn.getResponseCode();
            //API 호출이 정상 일 때
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.toString());
                JsonNode pathArray = jsonResponse.get("route").get("trafast").get(0).get("path");
                // 위도와 경도 정보 추출
                for (JsonNode locationNode : pathArray) {
                    double longitude = locationNode.get(0).asDouble();
                    double latitude = locationNode.get(1).asDouble();
                    result.add(SurveyResponseDto.builder().longtitude(longitude).latitude(latitude).build());
                }
            }
            else {
                throw new Exception("API호출 하는데 오류가 발생했습니다.");
            }
        }
        return result;
    }
}
