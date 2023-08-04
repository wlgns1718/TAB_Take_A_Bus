package com.ssafy.tab.controller;

import com.ssafy.tab.dto.NoticeResponseDto;
import com.ssafy.tab.service.NoticeService;
import com.ssafy.tab.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@Api("공지사항 컨트롤러 API")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;
//    private JwtTokenProvider jwtTokenProvider; // 토큰을 생성하고 검증하는 @Component (jjwt라이브러리로 구현할 수 있음)


    @ApiOperation(value = "공지사항 목록", notes = "공지사항 전체 목록을 보여줌(페이징)", response = Map.class)
    @GetMapping("/list")
    public Page<NoticeResponseDto> list(Pageable pageable){
        return noticeService.list(pageable);
    }


/*
    @ApiOperation(value = "공지사항 글쓰기", notes = "공지사항의 글을 작성한다.", response = Map.class)
    @PostMapping("/write")
    public ResponseEntity<Map<String,Object>> write(@RequestBody @ApiParam(value = "글작성(제목, 내용)",required = true) NoticeDto noticeDto, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
//        try {
//            String accessToken = request.getHeader("access-token");

//            jwtTokenProvider로 토큰이 유효한지 검사

//            jwtTokenProivder로 사용자 정보 가져오기
//            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
//            Optional<User> user = userService.findById(userId);

//            ADMIN인지 체크
//            String role = jwtTokenProvider.getUserRoleFromToken(token);


//            위 검증 과정을 다 거치면 write 수행

//            Notice notice = new Notice(user.get(),noticeDto.getTitle(),noticeDto.getContent(), LocalDateTime.now());

//            noticeService.createNotice(notice);


//            resultMap.put("code", "200");
//            resultMap.put("msg","글작성 성공!");
//        } catch (Exception e) {
//            resultMap.put("code", "500");
//            resultMap.put("msg","글작성 실패!");
//        }
//
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }

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
