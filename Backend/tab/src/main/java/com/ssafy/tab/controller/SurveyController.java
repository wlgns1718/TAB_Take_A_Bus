package com.ssafy.tab.controller;

import com.ssafy.tab.dto.SurveyDto;
import com.ssafy.tab.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    //나의 수요조사
    @GetMapping("/survey")
    public ResponseEntity<Map<String, Object>> selectMySurvey(HttpServletRequest requset) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
//        토큰을 받고 토큰으로 이용자를 받아오는 로직 수행.
//        System.out.println(requset.getHeader("TOKEN"));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //수요조사 저장
    @PostMapping("/survey")
    public ResponseEntity<Map<String, Object>> insertSurvey(@RequestBody SurveyDto surveyDto, HttpServletRequest requset) {
        Map<String, Object> resultMap = new HashMap<>();
        System.out.println(requset.getHeader("TOKEN"));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    //수요조사 삭제
    @DeleteMapping("/survey")
    public ResponseEntity<Map<String, Object>> deleteSurvey(@RequestBody SurveyDto surveyDto, HttpServletRequest requset) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(requset.getHeader("TOKEN"));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
}
