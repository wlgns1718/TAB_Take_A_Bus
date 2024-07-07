package com.ssafy.tab.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration // 스프링 실행시 설정파일
@EnableSwagger2 // Swagger2를 사용
@SuppressWarnings("unchecked") // warning 제거
public class SwaggerConfiguration implements WebMvcConfigurer {

//	Swagger-UI 2.x 확인
//	http://localhost[:8080]/{your-app-root}/swagger-ui.html
//	Swagger-UI 3.x 확인
//	http://localhost[:8080]/{your-app-root}/swagger-ui/index.html
	private String title = "Take A Bus API ";
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	} 

	private ApiInfo apiInfo() {
		String descript = "더 이상 버스를 놓치지 마세요!<br> 버스 승차를 도와주는 TAB입니다.";
		return new ApiInfoBuilder().title(title).description(descript)
				.licenseUrl("psg19lee@gmail.com").version("1.0").build();
	}
	// API마다 구분짓기 위한 설정.
	@Bean
	public Docket allApi() {
		return getDocket("전체", Predicates.or(PathSelectors.regex("/*.*")));
	}
	@Bean
	public Docket userApi() {
		return getDocket("회원", Predicates.or(PathSelectors.regex("/tab/user.*")));
	}

	@Bean
	public Docket noticeApi() {
		return getDocket("공지사항", Predicates.or(PathSelectors.regex("/tab/notice.*")));
	}

	@Bean
	public Docket boardApi() {
		return getDocket("게시판", Predicates.or(PathSelectors.regex("/tab/board.*")));
	}

	@Bean
	public Docket busApi() {
		return getDocket("버스", Predicates.or(PathSelectors.regex("/tab/station.*")));
	}

	@Bean
	public Docket surveyApi() {return getDocket("수요조사", Predicates.or(PathSelectors.regex("/tab/survey.*")));}
	@Bean
	public Docket ArduinoApi(){ return getDocket("아두이노" , Predicates.or(PathSelectors.regex("/tab/arduino.*")));}

	public Docket getDocket(String groupName, Predicate<String> predicate) {

		return new Docket(DocumentationType.SWAGGER_2).groupName(groupName).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ssafy")).paths(predicate)
				.apis(RequestHandlerSelectors.any()).build();
	}

	// swagger ui 설정.
	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
	}


}
