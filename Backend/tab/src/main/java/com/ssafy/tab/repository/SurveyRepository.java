package com.ssafy.tab.repository;


import com.ssafy.tab.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    /*
    수요조사 등록, 모두 가져오기 및 내가 작성한 수요조사만 가져오기
     */
    @Query("select s from Survey s where s.user.id = :user_no")
    List<Survey> findMySurvey(@Param("user_no") Long id);
}
