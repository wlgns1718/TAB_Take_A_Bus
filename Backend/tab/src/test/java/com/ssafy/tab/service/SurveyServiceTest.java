package com.ssafy.tab.service;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.Survey;
import com.ssafy.tab.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class SurveyServiceTest {

    @Autowired
    SurveyService surveyService;

    @Autowired
    UserService userService;

    @Test
    void createSurvey() throws Exception {
        User user = new User("mc.thd","123","송","thd@naver.com", Role.USER);
        user.setName("song");
        user.setUserId("mc.thd");
        user.setUserPw("123");

        //when
        userService.joinUser(user);

        Survey survey1 = new Survey();
        Survey survey2 = new Survey();
        Survey survey3 = new Survey();
        survey1.setUser(user);
        survey2.setUser(user);
        survey3.setUser(user);

        surveyService.createSurvey(survey1);
        surveyService.createSurvey(survey2);
        surveyService.createSurvey(survey3);

    }

    @Test
    void readAllSurvey() {
        //수요 조사 모두 불러오기
        List<Survey> surveyList = surveyService.selectAllSurvey();
        for (Survey survey : surveyList) {
            System.out.println("survey.getUser().getUserId() = " + survey.getUser().getUserId());
        }
    }
    
    @Test
    public void findMySurvey() throws Exception{
        User user = new User("mc.thd","123","송","thd@naver.com", Role.USER);
        userService.joinUser(user);

        Survey survey1 = new Survey();
        Survey survey2 = new Survey();
        Survey survey3 = new Survey();
        survey1.setUser(user);
        survey2.setUser(user);
        survey3.setUser(user);
        surveyService.createSurvey(survey1);
        surveyService.createSurvey(survey2);
        surveyService.createSurvey(survey3);
        System.out.println("user = " + user.getId());
        List<Survey> mySurveyList = surveyService.selectSurvey(user.getId());
        System.out.println("mySurveyList.size() = " + mySurveyList.size());
        for (Survey survey : mySurveyList) {
            System.out.println("survey = " + survey.getId());
        }
    }
}