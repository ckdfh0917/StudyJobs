package com.web.blog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalRestExceptionHandler {
	@ExceptionHandler(value={RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> internalServerError(Exception e){
		Map<String, Object> ret = new HashMap<>();
		ret.put("msg", e.getMessage());
		return ret;
	}
	
	@ExceptionHandler(value={ExpiredJwtException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Map<String, Object> expiredJwtError(Exception e){
		Map<String, Object> ret = new HashMap<>();
		ret.put("msg", e.getMessage());
		return ret;
	}
}
