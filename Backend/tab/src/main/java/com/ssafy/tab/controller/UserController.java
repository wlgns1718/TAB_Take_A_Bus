package com.ssafy.tab.controller;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.UserJoinDto;
import com.ssafy.tab.dto.UserLoginDto;
import com.ssafy.tab.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@RequestMapping("/tab/user")
@Api("사용자 컨트롤러  API")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService us;
    private final EmailService es;
    private final TokenBlacklistService tokenBlacklistService;

    private int refreshExpiredMs = 1000 * 60 * 600; // 10시간

/*
    @ApiOperation(value = "회원정보 by id", notes = "id를 이용하여 회원 정보를 반환한다.", response = Map.class)
    @GetMapping("/info/{userId}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("userId") @ApiParam(value = "조회할 회원의 아이디.", required = true) Long userId,
            HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            User user = us.findById(userId).get();

            resultMap.put("userInfo", userDto);
            resultMap.put("code", "200");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            logger.error("정보조회 실패 : {}", e);
            resultMap.put("code", "500");
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

 */
    @ApiOperation(value = "회원가입", notes = "회원가입 진행.", response = Map.class)
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@RequestBody @ApiParam(value = "회원가입에 필요한 정보", required = true) UserJoinDto userJoinDto){
        //logger.debug("join user : {} ", userDto);
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = new User(userJoinDto.getId(),userJoinDto.getPw(),userJoinDto.getName(),userJoinDto.getEmail(),userJoinDto.getRole());

            us.joinUser(user);

            resultMap.put("code", "200");
            resultMap.put("msg","회원가입 성공");
        }catch (IllegalStateException e){
            resultMap.put("data","중복된 아이디 입니다.");
            resultMap.put("msg","회원가입 실패");
            resultMap.put("code", "401");
        } catch (Exception e) {
            //logger.error("정보조회 실패 : {}", e);
            resultMap.put("code", "500");
            resultMap.put("msg","회원가입 실패");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }


    @ApiOperation(value = "로그인", notes = "token 과 로그인 결과를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @ApiParam(value = "로그인", required = true) UserLoginDto userLoginDto, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            Map<String, String> tokens = us.login(userLoginDto.getId(), userLoginDto.getPw());// 발행된 토큰
            String accessToken = tokens.get("accessToken");
            String refreshToken = tokens.get("refreshToken");
            Cookie cookie = new Cookie("refreshToken",refreshToken);
            cookie.setMaxAge(refreshExpiredMs);
            response.addCookie(cookie);

            Map<String,String> data = new HashMap<>();
            data.put("accessToken",accessToken);
            resultMap.put("data",data);
            resultMap.put("code", "200");
            resultMap.put("msg","로그인 성공");
        }catch (Exception e){
            resultMap.put("code", "500");
            resultMap.put("msg","로그인 실패");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }


    @ApiOperation(value = "accessToken 재발급", notes = "header에 있는 cookie refreshToken으로 accessToken 재발급 수행", response = Map.class)
    @PostMapping("/requestToken")
    public ResponseEntity<Map<String,Object>> requestToken(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")){
                    String refreshToken = cookie.getValue();
                    String newToken = us.requestToken(refreshToken);
                    if(newToken!=null){
                        resultMap.put("code","200");
                        resultMap.put("msg","accessToken을 성공적으로 받아왔습니다");
                        resultMap.put("data",newToken);
                    }else{
                        resultMap.put("code","401");
                        resultMap.put("msg","accessToken을 받아오는데 실패했습니다");
                    }

                    break;
                }
            }
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "로그아웃", notes = "token을 블랙리스트에 넣어서 로그아웃을 수행", response = Map.class)
    @DeleteMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") @ApiParam(value = "헤더에 있는 토큰", required = true) String authorizationHeader) {
        // Authorization 헤더에서 토큰 추출
        Map<String, Object> resultMap = new HashMap<>();

        String token = authorizationHeader.substring("Bearer ".length());
        // 토큰을 블랙리스트에 추가하여 무효화
        tokenBlacklistService.addToBlacklist(token);

        resultMap.put("code", "200");
        resultMap.put("msg","로그아웃이 성공적으로 처리되었습니다.");

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }


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
