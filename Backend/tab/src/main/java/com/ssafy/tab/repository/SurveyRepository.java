package com.ssafy.tab.repository;


import com.ssafy.tab.domain.Survey;
import com.ssafy.tab.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    /*
    수요조사 등록, 모두 가져오기 및 내가 작성한 수요조사만 가져오기
     */
    Optional<Survey> findByUser(User user);
}
