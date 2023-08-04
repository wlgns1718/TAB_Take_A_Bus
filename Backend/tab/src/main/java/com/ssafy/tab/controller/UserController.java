package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.UserDto;
import com.ssafy.tab.dto.UserLoginDto;
import com.ssafy.tab.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러  API")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService us;
    private final JwtService js;
    private final EmailService es;

    @ApiOperation(value = "회원가입", notes = "회원가입 진행.", response = Map.class)
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody @ApiParam(value = "회원가입에 필요한 정보", required = true) UserDto userDto){
        //logger.debug("join user : {} ", userDto);
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = new User(userDto.getId(),userDto.getPw(),userDto.getName(),userDto.getEmail(),userDto.getRole());

            us.joinUser(user);

            resultMap.put("code", "200");
            resultMap.put("msg","회원가입 성공");
        } catch (Exception e) {
            //logger.error("정보조회 실패 : {}", e);
            resultMap.put("code", "500");
            resultMap.put("msg","회원가입 실패");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    /*
    //@ApiOperation(value = "로그인", notes = "token 과 로그인 결과를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @ApiParam(value = "로그인", required = true) UserLoginDto userLoginDto){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            String token = us.login(userLoginDto.getId(), userLoginDto.getPw(), userLoginDto.getRole()); // 발행된 토큰

            resultMap.put("data",token);
            resultMap.put("code", "200");
            resultMap.put("msg","로그인 성공");
        }catch (Exception e){
            resultMap.put("code", "500");
            resultMap.put("msg","로그인 실패");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }*/






    /*@ApiOperation(value = "이메일 인증코드 전송", notes = "전송한 인증코드를 반환한다.", response = Map.class)
    @PostMapping("/sendmail")
    public ResponseEntity<Map<String, Object>> sendMail(@RequestBody EmailDto emailDto){

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            String code = "error";
            UserDto userDto = us.getUser(emailDto.getUserId());
            if(userDto.getEmail().equals(emailDto.getEmail())) {
                code = es.sendMail(emailDto);
            }

            if(code.equals("error")) {
                resultMap.put("code","401");
                status = HttpStatus.ACCEPTED;
            }else {
                if(emailDto.getType().equals("register")) {
                    resultMap.put("emailCode", code);
                }else if(emailDto.getType().equals("findPw")) {
                    userDto = new UserDto();
                    userDto.setPassword(code);
                    userDto.setUserId(emailDto.getUserId());
                    userDto.setEmail(emailDto.getEmail());
                    System.out.println(userDto);
                    us.findPw(userDto);
                }
                status = HttpStatus.ACCEPTED;
                resultMap.put("code", "200");
            }
        }catch(Exception e) {
            resultMap.put("code", "500");
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }*/


}
