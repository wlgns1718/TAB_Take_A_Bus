package com.ssafy.tab.controller;

import com.ssafy.tab.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    SurveyService surveyService;

    //저장하기.
//    @PostMapping("/survey/add")

}
