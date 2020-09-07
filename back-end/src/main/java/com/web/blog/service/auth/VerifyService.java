package com.web.blog.service.auth;

import com.web.blog.model.auth.Mail;

public interface VerifyService {
	public boolean isDuplicated(final String email);
	public boolean isValidUser(final String email, final String password);
	public boolean isValidCode(final String email, final String code);
	public Mail generateVerifyMail(final String email);
}
