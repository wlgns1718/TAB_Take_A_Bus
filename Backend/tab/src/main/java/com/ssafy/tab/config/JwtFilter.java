package com.ssafy.tab.config;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.service.UserService;
import com.ssafy.tab.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter { // 토큰이 있는지 매번 인증을 해야 함 -> extends OncePerRequestFilter

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰을 꺼내서 검사한 후 막거나 허용하는 코드
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("header에서 가져온 authorization : ",authorization);

        if(authorization==null || !authorization.startsWith("Bearer ")){ // 토큰이 안 넘어 왔으면 권한 부여 코드까지 가지 않고 return
            log.info("authorization == null || !authorization.startsWith(Bearer)");
            filterChain.doFilter(request,response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];

        // Token Expired되었는지 여부
        if(JwtUtil.isExpired(token,secretKey)){
            log.info("Token이 만료되었습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        // UserId Token에서 꺼내기
        String userId = JwtUtil.getUserId(token,secretKey);
        log.info("userId:{}" + userId);

        // 권한 부여
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString())); // authority 종류
        authorities.add(new SimpleGrantedAuthority(Role.MANAGER.toString()));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId,null,authorities);

        // Detail을 넣어 줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
