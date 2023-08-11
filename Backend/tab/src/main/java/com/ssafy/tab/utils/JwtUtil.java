package com.ssafy.tab.utils;


// jjwt 라이브러리를 이용하여 Jwt 로직 구현하는 클래스

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


public class JwtUtil {

    public static boolean isExpired(String token, String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public static String createToken(String userId, String secretKey,Long expiredMs){ // 토큰에 담을 정보
        Claims claims = Jwts.claims(); // 일종의 맵 (데이터를 저장하는)
        claims.put("userId",userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256,secretKey) // HS256 알고리즘으로 암호화
                .compact();
    }


    public static String getUserId(String token, String secretKey){

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userId",String.class);
    }

}

