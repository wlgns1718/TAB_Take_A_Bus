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
        return (web) -> web.ignoring().antMatchers("/v2/api-docs","/configuration/ui","/swagger-resources","/configuration/security","/swagger-ui.html","/webjars/**","/swagger/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()// / Http basic Auth  기반으로 로그인 인증창이 뜸.  disable 시에 인증창 뜨지 않음
                .csrf().disable()// rest api이므로 csrf 보안이 필요없으므로 disable처리
                .cors().and()
                .authorizeRequests()// request를 authorize하겠다
                .antMatchers("/tab/user/join","/tab/user/login","/tab/user/logout","/tab/user/requestToken","/tab/user/checkId",
                        "/tab/notice/list","/tab/notice/detail/**",
                        "/tab/station/**","/tab/arduino/**","/tab/user/mail/**"
                        ).permitAll() // 누구나 접근가능
                .antMatchers(HttpMethod.GET,"/tab/board/**","/tab/board/content/**","/tab/board/sort/**","/tab/board/title/**","/tab/board/user/**").permitAll()
                .antMatchers("/notice/modify/**","/tab/notice/write","/tab/notice/delete/**","/tab/survey","/tab/survey/all","/tab/user/update").authenticated()
                .antMatchers(HttpMethod.POST,"/tab/board","/tab/board/**/comment").authenticated() // 인증이 필요한 경로
                .antMatchers(HttpMethod.PUT,"/tab/board/**","/tab/board/**/comment/**").authenticated() // 인증이 필요한 경로
                .antMatchers(HttpMethod.DELETE,"/tab/board/**","/tab/board/**/comment/**").authenticated() // 인증이 필요한 경로
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(userService,tokenBlacklistService,secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}

