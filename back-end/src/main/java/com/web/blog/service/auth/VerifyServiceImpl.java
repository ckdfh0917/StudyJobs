package com.web.blog.service.auth;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.blog.dao.auth.VerifyDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.auth.Mail;
import com.web.blog.model.auth.Verify;

@Service
public class VerifyServiceImpl implements VerifyService {
	@Value("${verify.lenCode}")
	private int lenCode;
	
	@Autowired
	private VerifyDao verifyDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public boolean isDuplicated(final String email) {
		return userDao.findUserByEmail(email).isPresent();
	}

	@Override
	public boolean isValidUser(final String email, final String password) {
		final String encodedPassword = userDao.findPasswordByEmail(email);
		return encodedPassword==null ? false : passwordEncoder.matches(password, encodedPassword);
//		return userDao.findUserByEmailAndPassword(email, password).isPresent();
	}

	@Override
	public boolean isValidCode(final String email, final String code) {
		return verifyDao.findVerifyByEmailAndCode(email, code).isPresent();
	}

	@Override
	public Mail generateVerifyMail(final String email) {
		String code = generateCode();
		verifyDao.save(new Verify(email, code));
		
		Mail mail = new Mail();
		mail.setTo(email);
		mail.setTitle("[SSAFY Web Blog] - verify your email");
		mail.setMessage(code);
		return mail;
	}
	
	public String generateCode() {
		Random random = new Random(System.currentTimeMillis());
		int range = (int)Math.pow(10, lenCode);
		int trim  = (int)Math.pow(10, lenCode-1);
		int code = random.nextInt(range) + trim;
		code = code > range ? code-trim : code;
		return String.valueOf(code);
	}
}
