package com.ssafy.tab.controller;

import com.ssafy.tab.dto.SurveyDto;
import com.ssafy.tab.dto.SurveyResponseDto;
import com.ssafy.tab.service.SurveyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tab/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    //나의 수요조사 가져오기
    @ApiOperation(value = "나의 수요조사 가져오기", notes = "내가 등록했던 수요조사를 가져옵니다. 오로지 하나만 가능", response = Map.class)
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> selectMySurvey(Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try{
            SurveyDto surveyDto = surveyService.selectMySurvey(userId);
            resultMap.put("msg", "회원님이 등록하신 수요조사를 가져왔습니다!");
            resultMap.put("code", "200");
            resultMap.put("data", surveyDto);
        }catch (Exception e){
            resultMap.put("msg", "회원님께서는 아직 수요조사를 등록하지 않으셨습니다!");
            resultMap.put("code", "500");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //수요조사 저장
    @ApiOperation(value = "수요조사 등록", notes = "출발지 경도 위도, 목적지 경도 위도를 설정해 수요조사를 합니다.", response = Map.class)
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> insertSurvey(@RequestBody @ApiParam(value = "수요 조사에 필요한 요소", required = true) SurveyDto surveyDto, Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try {
            surveyService.selectMySurvey(userId);
            resultMap.put("msg", "이미 등록하셨습니다. 삭제를 먼저 하세요!");
            resultMap.put("code", "500");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }catch (Exception e){
            try{
                surveyService.createSurvey(surveyDto, userId);
                resultMap.put("msg", "수요조사 등록 완료!");
                resultMap.put("code", "200");
            }catch (Exception E){
                resultMap.put("msg", "수요조사 등록 실패했습니다. 이미 등록하셨습니다. 삭제를 먼저 하세요!");
                resultMap.put("code", "500");
            }
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
        }
    }

    //수요조사 삭제
    @ApiOperation(value = "수요조사 삭제", notes = "이미 등록했던 나의 수요조사를 삭제합니다.", response = Map.class)
    @DeleteMapping("")
    public ResponseEntity<Map<String, Object>> deleteSurvey(Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try{
            surveyService.deleteSurvey(userId);
            resultMap.put("msg", "수요조사 삭제 완료!");
            resultMap.put("code", "200");
        }catch (Exception e){
            resultMap.put("msg", "수요조사 삭제 실패!");
            resultMap.put("code", "500");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //전체 수요 조사에 대한 최적의 경로 가져오기
    @ApiOperation(value = "모든 수요조사 가져오기", notes = "등록된 모든 수요조사를 가져와서 네이버 API를 사용해서 최단 경로로 만들어주고 구글 API로 뿌려주기.", response = Map.class)
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> selectAllSurvey(Authentication authentication) {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = authentication.getName();
        try{
            List<SurveyResponseDto> bestRoute = surveyService.findBestRoute();
            resultMap.put("data", bestRoute);
            resultMap.put("msg", "수요조사를 가져오는데 성공했습니다!");
            resultMap.put("code", "200");
        }catch (Exception e){
            String errorMessage = e.getMessage();
            resultMap.put("msg", "수요조사를 가져오는데 오류가 발생했습니다.");
            resultMap.put("code", "500");
            resultMap.put("errorMessage", errorMessage);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
}
