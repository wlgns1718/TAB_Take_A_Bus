package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@Api("공지사항 컨트롤러 API")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
//    private JwtTokenProvider jwtTokenProvider; // 토큰을 생성하고 검증하는 @Component (jjwt라이브러리로 구현할 수 있음)
    @ApiOperation(value = "공지사항 글쓰기", notes = "공지사항의 글을 작성한다.", response = Map.class)
    @PostMapping("/write")
    public ResponseEntity<Map<String,Object>> write(@RequestBody @ApiParam(value = "글작성(제목, 내용)",required = true) NoticeDto noticeDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String accessToken = request.getHeader("access-token");

//            jwtTokenProvider로 토큰이 유효한지 검사

//            jwtTokenProivder로 사용자 정보 가져오기 , ADMIN인지 체크 ex) jwtTokenProvider.getUserRoleFromToken(token)

//            String userId = jwtTokenProvider.getUserIdFromToken(accessToken);

//             위 검증 과정을 다 거치면 noticeService.write()수행

//            Notice notice = new Notice(noticeDto.getTitle(),noticeDto.getContent(), LocalDateTime.now());



            resultMap.put("code", "200");
            resultMap.put("msg","글작성 성공!");
        } catch (Exception e) {
            resultMap.put("code", "500");
            resultMap.put("msg","글작성 실패!");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }



}
