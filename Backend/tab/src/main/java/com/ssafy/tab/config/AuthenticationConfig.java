package com.ssafy.tab.config;


import com.ssafy.tab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserService userService;
    @Value("${jwt.secret}")
    private String secretKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()// / Http basic Auth  기반으로 로그인 인증창이 뜸.  disable 시에 인증창 뜨지 않음
                .csrf().disable()// rest api이므로 csrf 보안이 필요없으므로 disable처리
                .cors().and()
                .authorizeRequests()// request를 authorize하겠다
                .antMatchers("/user/join","/user/login","/notice/list").permitAll() // 누구나 접근가능
//                .antMatchers("/api/stops/**").authenticated()
                .antMatchers(HttpMethod.POST,"/user/**","/notice/write").authenticated() // 인증이 필요한 경로
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(userService,secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

    }



}

