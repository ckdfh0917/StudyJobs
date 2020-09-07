package com.web.blog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.web.blog.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Autowired
	private JwtInterceptor jwtInterceptor;

	private final String[] exclude = {"/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", // 개발 완료시 삭제바랍니다.
									  "/login",
									  "/verify/**",
									  "/address/**",
									  "/resume/all",
									  "/study/all",
									  "/study/{studyId:[0-9]+}",
									  "/user/signUp",
									  "/user/help"};
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
		        .addPathPatterns("/**")
		        .excludePathPatterns(Arrays.asList(exclude));							   
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:8080","http://i3a607.p.ssafy.io/")
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("jwt-auth-token", "user-email");
	}
}
