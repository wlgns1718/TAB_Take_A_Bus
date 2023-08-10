package com.ssafy.tab.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RestController;
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
public class SwaggerConfiguration {

//	Swagger-UI 2.x 확인
//	http://localhost[:8080]/{your-app-root}/swagger-ui.html
//	Swagger-UI 3.x 확인
//	http://localhost[:8080]/{your-app-root}/swagger-ui/index.html

	private String title = "Take A Bus API ";

	private ApiInfo apiInfo() {
		String descript = "더 이상 버스를 놓치지 마세요!<br> 버스 승차를 도와주는 TAB입니다.";
		return new ApiInfoBuilder().title(title).description(descript)
				.licenseUrl("psg19lee@gmail.com").version("1.0").build();
	}

	/*

	//  Swagger에 Authorization 설정
	private static final String REFERENCE="Authorization 헤더 값";
	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build()
				.securityContexts(Arrays.asList(securityContext()))
	}

	private SecurityContext securityContext(){
		return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth())
				.operationSelector
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
		return Arrays.asList(new SecurityReference(REFERENCE, authorizationScopes));
	}
	*/

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

	public Docket getDocket(String groupName, Predicate<String> predicate) {
//		List<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();
//		responseMessages.add(new ResponseMessageBuilder().code(200).message("OK !!!").build());
//		responseMessages.add(new ResponseMessageBuilder().code(500).message("서버 문제 발생 !!!").responseModel(new ModelRef("Error")).build());
//		responseMessages.add(new ResponseMessageBuilder().code(404).message("페이지를 찾을 수 없습니다 !!!").build());
		return new Docket(DocumentationType.SWAGGER_2).groupName(groupName).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ssafy")).paths(predicate)
				.apis(RequestHandlerSelectors.any()).build();
//				.useDefaultResponseMessages(false)
//				.globalResponseMessage(RequestMethod.GET,responseMessages);
	}

	// swagger ui 설정.
	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
	}


}
