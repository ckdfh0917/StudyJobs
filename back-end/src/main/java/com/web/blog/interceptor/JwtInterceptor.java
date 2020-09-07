package com.web.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.web.blog.service.auth.JwtService;

@Component
public class JwtInterceptor implements HandlerInterceptor{
	@Autowired
	private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws Exception{
		if(request.getMethod().equals("OPTIONS")) return true;
		
		final String token = request.getHeader("jwt-auth-token");
		final String email = request.getHeader("user-email");
		
		if(token==null || token.length()==0) throw new RuntimeException("토큰이 존재하지 않습니다.");
		else if(token.equals("lotiple")) return true; // 개발 완료시 삭제바랍니다.
		else if(email==null || email.length()==0) throw new RuntimeException("사용자 정보가 존재하지 않습니다.");
		else if(jwtService.isValidToken(token, email)==false) throw new RuntimeException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
		else return true;
	}
}
