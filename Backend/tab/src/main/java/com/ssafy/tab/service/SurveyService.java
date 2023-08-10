package com.ssafy.tab.service;

import com.ssafy.tab.domain.Survey;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.SurveyDto;
import com.ssafy.tab.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService{

    private final UserService userService;
    private final SurveyRepository surveyRepository;

    //사용자의 수요 등록
    public void createSurvey(SurveyDto surveyDto, String userId){
        User user = userService.findByUserId(userId);
        Survey survey = new Survey(user, LocalDateTime.now(), surveyDto.getStartLatitude(), surveyDto.getStartLongtitude(), surveyDto.getDestinationLatitude(), surveyDto.getDestinationLongtitude());
        surveyRepository.save(survey);
    }

    //내 수요조사 삭제하기
    public void deleteSurvey(String userId){
        surveyRepository.delete(surveyRepository.findByUser(userService.findByUserId(userId)).get());
    }

    //내가 작성한 수요조사만 가져오기.
    @Transactional(readOnly = true)
    public SurveyDto selectMySurvey(String userId) throws Exception {
        Optional<Survey> tempSurvey = surveyRepository.findByUser(userService.findByUserId(userId));
        if (tempSurvey.isPresent()){
            Survey survey = tempSurvey.get();
            SurveyDto surveyDto = new SurveyDto(survey.getCreateDate(), survey.getStartLatitude(), survey.getStartLontitude(), survey.getDestinationLatitude(), survey.getDestinationLongtitude());
            return surveyDto;
        }else{
            throw new Exception();
        }
    }

    //모든 수요조사를 가져오기
    @Transactional(readOnly = true)
    public List<Survey> selectAllSurvey() {
        return surveyRepository.findAll();
    }
}
