package com.ssafy.tab.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.ssafy.tab.domain.Role.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {

    private final UserService userService;

    @Value("${kakao.rest.api}")
    private String rest_api;
    @Value("${kakao.redirect.key}")
    private String redirect_key;
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${kakao.client.secret.key}")
    private String client_secret_key;


    private static Long accessExpiredMs = 1000 * 60 * 60l; // 1시간
    private static Long refreshExpiredMs = 1000 * 60 * 600l; // 10시간

    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + rest_api); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=" + redirect_key); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            sb.append("&client_secret=" + client_secret_key);
            bw.write(sb.toString());
            bw.flush();
            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            System.out.println();

            if(responseCode != 200){
                throw new Exception();
            }

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return access_Token;
    }

    public Map<String, String> getUserInfo(String access_Token) {
        //요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        Map<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            String id = element.getAsJsonObject().get("id").getAsString();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean();
            String email = "";
            //이미 DB에 저장 되어 있을 때
            if (userService.findByUserId(id) != null) {
                User user = userService.findByUserId(id);
                String accessToken = JwtUtil.createToken(id, secretKey, accessExpiredMs);
                String refreshToken = JwtUtil.createToken(id, secretKey, refreshExpiredMs);
                userInfo.put("id", id);
                userInfo.put("accessToken", accessToken);
                userInfo.put("refreshToken", refreshToken);
                user.setRefreshToken(refreshToken);
                userInfo.put("msg", "원래 있던 회원입니다!");
            }
            //이미 DB에 저장이 되어 있지 않을 때
            else {
                //이메일은 가입한 사람이 동의해야 얻어 올 수 있음.
                User joinUser = new User(id, nickname, email, USER);
                if (!hasEmail) {
                    System.out.println(4);
                    email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
                    joinUser.setEmail(email);
                    System.out.println(email);
                    userService.joinUserKakao(joinUser);
                    System.out.println(5);
                } else {
                    System.out.println(6);
                    userService.joinUserKakao(joinUser);
                    System.out.println(7);
                }
                System.out.println(8);
                userInfo.put("msg", "새로운 회원으로 가입 되었습니다!");
                String accessToken = JwtUtil.createToken(id, secretKey, accessExpiredMs);
                String refreshToken = JwtUtil.createToken(id, secretKey, refreshExpiredMs);
                joinUser.setRefreshToken(refreshToken);
                userInfo.put("id", id);
                userInfo.put("accessToken", accessToken);
                userInfo.put("refreshToken", refreshToken);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userInfo;
    }
}