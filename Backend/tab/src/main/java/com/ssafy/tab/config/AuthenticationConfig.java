package com.ssafy.tab.config;


import com.ssafy.tab.service.TokenBlacklistService;
import com.ssafy.tab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
<<<<<<< HEAD
        return (web) -> web.ignoring().antMatchers("/user/join","/user/login","/notice/list","/api/stops/**","/user/requestToken",
                "/swagger-ui.html#/**","/swagger-ui.html");
=======
        return (web) -> web.ignoring().antMatchers("/user/join","/user/login","/notice/list","/notice/detail/**","/api/stops/**","/user/requestToken");
>>>>>>> peter
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()// / Http basic Auth  기반으로 로그인 인증창이 뜸.  disable 시에 인증창 뜨지 않음
                .csrf().disable()// rest api이므로 csrf 보안이 필요없으므로 disable처리
                .cors().and()
                .authorizeRequests()// request를 authorize하겠다
<<<<<<< HEAD
                .antMatchers("/user/join","/user/login","/notice/list","/api/stops/**",
                        "/swagger-ui.html#/**","/swagger-ui.html"
                        ).permitAll() // 누구나 접근가능
=======
                .antMatchers("/notice/detail/**","/user/join","/user/login","/notice/list","/api/stops/**").permitAll() // 누구나 접근가능
>>>>>>> peter
                .antMatchers("/notice/modify/**").authenticated()
                .antMatchers(HttpMethod.POST,"/user/**","/notice/write").authenticated() // 인증이 필요한 경로
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(userService,tokenBlacklistService,secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

    }



}

