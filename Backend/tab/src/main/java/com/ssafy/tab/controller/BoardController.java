package com.ssafy.tab.controller;


import com.ssafy.tab.domain.Board;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.dto.SurveyDto;
import com.ssafy.tab.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    BoardService boardService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> insertBoard(@RequestBody BoardDto boardDto, HttpServletRequest requset) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(requset.getHeader("TOKEN"));
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
}
