package com.ssafy.tab.service;

import com.ssafy.tab.domain.Survey;
import com.ssafy.tab.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService{

    private final SurveyRepository surveyRepository;

    //사용자의 수요 등록
    public Survey createSurvey(Survey survey){
        surveyRepository.save(survey);
        return survey;
    }

    //모든 사용자의 수요 가져오기
    @Transactional(readOnly = true)
    public List<Survey> readAllSurvey(){
        List<Survey> surveyList= surveyRepository.findAll();
        return surveyList;
    }

    //내가 작성한 수요조사만 가져오기.
    @Transactional(readOnly = true)
    public List<Survey> findMySurvey(Long id){
        List<Survey> mySurveyList = surveyRepository.findMySurvey(id);
        return mySurveyList;
    }
}
