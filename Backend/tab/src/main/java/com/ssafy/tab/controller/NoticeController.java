package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.dto.NoticeResponseDto;
import com.ssafy.tab.service.NoticeService;
import com.ssafy.tab.service.UserService;
import com.ssafy.tab.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notice")
@Api("공지사항 컨트롤러 API")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;



    @ApiOperation(value = "공지사항 목록", notes = "공지사항 전체 목록을 보여줌(페이징)", response = Map.class)
    @GetMapping("/list")
    public Page<NoticeResponseDto> list(Pageable pageable){
        return noticeService.list(pageable);
    }


    @ApiOperation(value = "공지사항 글쓰기", notes = "공지사항의 글을 작성한다.", response = Map.class)
    @PostMapping("/write")
    public ResponseEntity<Map<String,Object>> write(@RequestBody @ApiParam(value = "글작성(제목, 내용)",required = true) NoticeDto noticeDto, HttpServletRequest request, Authentication authentication){
        Map<String, Object> resultMap = new HashMap<>();
        try {

//          Authentication으로 사용자 정보 가져오기
            String userId = authentication.getName(); // Authentication.getName()으로 토큰에 담긴 정보를 받아올 수 있음
            User user = userService.findByUserId(userId);

//          MANAGER 인지 체크
            if(user.getRole() == Role.MANAGER){

                Notice notice = new Notice(user,noticeDto.getTitle(),noticeDto.getContext(), Timestamp.valueOf(LocalDateTime.now()));
                noticeService.createNotice(notice);
                resultMap.put("code", "200");
                resultMap.put("msg","글작성 성공!");
            }else{
                resultMap.put("code", "401");
                resultMap.put("msg","관리자만 글을 작성할 수 있습니다.");
            }


        } catch (Exception e) {
            resultMap.put("code", "500");
            resultMap.put("msg","글작성 실패!");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }
/*

    @ApiOperation(value = "공지사항 글수정", notes = "공지사항의 글을 수정.", response = Map.class)
    @PutMapping("/modify/${id}")
    public ResponseEntity<Map<String,Object>> write(@PathVariable(name = "id")  Long noticeNo, @RequestBody @ApiParam(value = "글수정(제목, 내용)",required = true) NoticeDto noticeDto, HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
//        try {
//            String accessToken = request.getHeader("access-token");

//            jwtTokenProvider로 토큰이 유효한지 검사

//            jwtTokenProivder로 사용자 정보 가져오기
//            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
//            Optional<User> user = userService.findById(userId);

//            ADMIN인지 체크
//            String role = jwtTokenProvider.getUserRoleFromToken(token);


//            위 검증 과정을 다 거치면 modify 수행
//            Notice notice = noticeService.findById(noticeNo);
//            notice.changeTitle(noticeDto.getTitle());
//            notice.changeContent(noticeDto.getContent());
//            notice.changeTime(LocalDateTime.now());


//
//
//            resultMap.put("code", "200");
//            resultMap.put("msg","글작성 성공!");
//        } catch (Exception e) {
//            resultMap.put("code", "500");
//            resultMap.put("msg","글작성 실패!");
//        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }
*/
}
