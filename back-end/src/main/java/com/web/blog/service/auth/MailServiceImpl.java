package com.web.blog.service.auth;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.web.blog.model.auth.Mail;

@Service
public class MailServiceImpl implements MailService{
	@Value("${spring.mail.protocol}")
	private String protocol;
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private int port;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Async
	@Override
	public void sendMail(Mail mail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(username);
		message.setTo(mail.getTo());
		message.setSubject(mail.getTitle());
		message.setText(mail.getMessage());
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setProtocol(protocol);
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");

		mailSender.setJavaMailProperties(prop);
		mailSender.send(message);
	}
}
