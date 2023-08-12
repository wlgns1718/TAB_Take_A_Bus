package com.ssafy.tab.controller;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.*;
import com.ssafy.tab.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tab/user")
@Api("사용자 컨트롤러  API")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService us;
    private final EmailService es;
    private final TokenBlacklistService tokenBlacklistService;
    private final OAuthService kakao;

    private int refreshExpiredMs = 1000 * 60 * 600; // 10시간


    @ApiOperation(value = "회원정보 수정", notes = "회원정보 수정 진행.", response = Map.class)
    @PutMapping("/update/{userId}")
    public ResponseEntity<Map<String,Object>> update(@PathVariable("userId")String userId, @RequestBody @ApiParam(value = "수정하고 싶은 정보만 입력하면 됩니다.", required = true) UserUpdateDto userUpdateDto, Authentication authentication){

        Map<String, Object> resultMap = new HashMap<>();
        try {
            String tokenUserId = authentication.getName();
            if(tokenUserId.equals(userId)){
                if(us.updateUser(userId,userUpdateDto)){
                    resultMap.put("code", "200");
                    resultMap.put("msg","회원정보 수정 성공");
                }else{
                    resultMap.put("code", "401");
                    resultMap.put("msg",userId + "에 해당하는 사용자가 존재하지 않습니다");
                }
            }else{
                resultMap.put("code", "401");
                resultMap.put("msg","본인의 정보만 수정할 수 있습니다.");
            }
        }catch (Exception e){
            resultMap.put("code", "500");
            resultMap.put("msg","회원정보 수정 실패");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }


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


    @ApiOperation(value = "로그인", notes = "accessToken과 refreshToken을 반환 (refreshToken을 header의 쿠키에 넣어서 반환)", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @ApiParam(value = "로그인", required = true) UserLoginDto userLoginDto, HttpServletResponse response){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            Map<String, String> result = us.login(userLoginDto.getId(), userLoginDto.getPw());// 발행된 토큰
            String accessToken = result.get("accessToken");
            String refreshToken = result.get("refreshToken");
            String role = result.get("role");
            Cookie cookie = new Cookie("refreshToken",refreshToken);
            cookie.setMaxAge(refreshExpiredMs);
            response.addCookie(cookie);

            Map<String,String> data = new HashMap<>();
            data.put("accessToken",accessToken);
            data.put("role",role);
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

    @ApiOperation(value = "아이디 중복검사", notes = "해당 아이디 중복검사", response = Map.class)
    @GetMapping("/checkId/{id}")
    public ResponseEntity<Map<String,Object>> checkId(@PathVariable("id")String id){
        Map<String, Object> resultMap = new HashMap<>();

        try{
            if(us.checkId(id)){
                resultMap.put("code","200");
                resultMap.put("msg",id+"는 사용가능한 아이디 입니다.");
            }else{
                resultMap.put("code","401");
                resultMap.put("msg",id+"는 중복된 아이디 입니다.");
            }
        }catch (Exception e){
            resultMap.put("code","500");
            resultMap.put("msg","아이디 중복검사 실패");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);

    }

    @ApiOperation(value = "이메일 인증코드 전송", notes = "이메일을 통한 본인확인과 임시 비밀번호 발급 기능을 수행합니다.", response = Map.class)
    @PostMapping("/mail")
    public ResponseEntity<Map<String, Object>> sendMail(@ApiParam(value = "type 은 \"register\"(회원가입 본인확인에 사용, userId 입력 불필요) 혹은 \"tempPw\"(임시 비밀번호 발급에 사용)", required = true) @RequestBody EmailDto emailDto){

        Map<String, Object> resultMap = new HashMap<>();
        String code;

        try {
            if (emailDto.getType().equals("register")) {

                code = es.sendMail(emailDto);

                resultMap.put("data",code);
                resultMap.put("msg", "본인 확인용 코드입니다.");
                resultMap.put("code", "200");
            } else if (emailDto.getType().equals("tempPw")) {
                User user = us.findByUserId(emailDto.getUserId());
                if (user.getEmail().equals(emailDto.getEmail())) { // 입력받은 사용자의 이메일 일치여부 검사
                    code = es.sendMail(emailDto);
                    us.updatePw(user.getUserId(),code); // 비밀번호 변경
                    resultMap.put("msg", "이메일로 임시 비밀번호를 발급했습니다.");
                    resultMap.put("code", "200");
                }else{
                    resultMap.put("msg", "회원가입 시 입력한 이메일과 일치하지 않습니다.");
                    resultMap.put("code", "401");
                }
            }else{
                resultMap.put("msg", "잘못된 전송형식 입니다");
                resultMap.put("code", "401");
            }

        }catch (Exception e){
            resultMap.put("msg", "이메일 전송에 실패했습니다.");
            resultMap.put("code", "500");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }


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

    @ApiOperation(value = "카카오 로그인", notes = "카카오에서 인증 코드를 받아와서 서버에서 처리", response = Map.class)
    @PostMapping(value="/login/kakao")
    public Map<String, Object> login(@RequestBody @ApiParam(value = "카카오 서버에서 인증을 받은 후 헤더에 있는 code", required = true) KakaoUserDto kakaoUserDto, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String code = kakaoUserDto.getCode();
        try {
            String access_Token = kakao.getKakaoAccessToken(code);
            Map<String, String> userInfo = kakao.getUserInfo(access_Token);
            String accessToken = userInfo.get("accessToken");
            String refreshToken = userInfo.get("refreshToken");
            Cookie cookie = new Cookie("refreshToken",refreshToken);
            cookie.setMaxAge(refreshExpiredMs);
            response.addCookie(cookie);
            Map<String,String> data = new HashMap<>();
            data.put("accessToken",accessToken);
            resultMap.put("data",data);
            resultMap.put("code", "200");
            resultMap.put("msg","카카오 로그인 성공");
            resultMap.put("msg2", userInfo.get("msg"));
        }
        catch (Exception e){
            resultMap.put("code", "500");
            resultMap.put("msg","카카오 로그인 실패");
        }
        return resultMap;
    }

}
