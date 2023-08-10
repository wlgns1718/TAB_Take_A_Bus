package com.ssafy.tab.config;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.service.TokenBlacklistService;
import com.ssafy.tab.service.UserService;
import com.ssafy.tab.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter { // 토큰이 있는지 매번 인증을 해야 함 -> extends OncePerRequestFilter

    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    private final String secretKey;
 
    private String refreshToken;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰을 꺼내서 검사한 후 막거나 허용하는 코드

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization==null || !authorization.startsWith("Bearer ")){ // 토큰이 null이거나 Bearer으로 시작하지 않으면

            log.error("authorization을 잘못 보냈습니다.");
//            response.setCharacterEncoding("UTF-8");
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//            String errorResponse = "{\"code\" : \"401\", \"msg\":\"accessToken이 없습니다.\"}";
//            response.getWriter().write(errorResponse);
            filterChain.doFilter(request,response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];


        try {
            JwtUtil.isExpired(token, secretKey);
        }
        catch (ExpiredJwtException e){
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }
        catch (MalformedJwtException e){
            log.error("올바른 토큰이 아닙니다.");
            filterChain.doFilter(request,response);
            return;
        }



        if(tokenBlacklistService.isTokenBlacklisted(token)){ // 로그아웃 로직 (블랙리스트에 등록된 토큰)

            log.error("로그아웃 된 사용자입니다.");
            filterChain.doFilter(request,response);
            return;

        }



        //  token에서 userId 꺼내기
        String userId = JwtUtil.getUserId(token,secretKey);

        if(userId==null){
            log.error("token에서 userId를 가져오지 못했습니다");
            filterChain.doFilter(request,response);
            return;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(Role.MANAGER.toString()));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId,null,authorities);

        // Detail을 넣어 줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }

}
