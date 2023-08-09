package com.ssafy.tab.config;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.service.TokenBlacklistService;
import com.ssafy.tab.service.UserService;
import com.ssafy.tab.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
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

        if(authorization==null || !authorization.startsWith("Bearer ")){ // 토큰이 안 넘어 왔으면 권한 부여 코드까지 가지 않고 return
            log.info("authorization is null or not startsWith Bearer");
            filterChain.doFilter(request,response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];


        try{
            JwtUtil.isExpired(token,secretKey);
        }catch (ExpiredJwtException e){
            if(request.getRequestURI().equals("/user/login") || request.getRequestURI().equals("/user/refresh")){
                filterChain.doFilter(request,response);
                return;
            }
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String errorResponse = "{\"code\" : \"401\", \"msg\":\"accessToken이 만료되었습니다.\"}";
            PrintWriter writer = response.getWriter();
            writer.write(errorResponse);
            writer.flush();
            return;
        }

        if(tokenBlacklistService.isTokenBlacklisted(token)){ // 로그아웃 로직 (블랙리스트에 등록된 토큰)
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String errorResponse = "{\"code\" : \"401\", \"msg\":\"로그아웃 되었습니다. 다시 로그인해주세요.\"}";
            PrintWriter writer = response.getWriter();
            writer.write(errorResponse);
            writer.flush();
            return;
        }



        //  token에서 userId 꺼내기 ( userId가 null이면 잘못된 refreshToken이다. refreshToken에는 사용자 정보를 담지 않음 )
        String userId = JwtUtil.getUserId(token,secretKey);
        if(userId==null){
            log.info("잘못된 accessToken입니다.");
            filterChain.doFilter(request,response);
            return;
        }

        log.info("userId : " + userId);

        setAuthentication(request, userId);

        //SecurityContextHolder에 authentication이 없으면 현재 정보를 바탕으로 setAuthentication
//        if(SecurityContextHolder.getContext().getAuthentication()==null) {
//            setAuthentication(request, userId);
//        }

        filterChain.doFilter(request,response);
    }

    private void setAuthentication(HttpServletRequest request,String userId){

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString())); // authority 종류
        authorities.add(new SimpleGrantedAuthority(Role.MANAGER.toString()));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId,null,authorities);
        // Detail을 넣어 줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
