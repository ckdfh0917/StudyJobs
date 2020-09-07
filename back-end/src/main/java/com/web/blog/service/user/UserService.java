package com.web.blog.service.user;

import java.util.Map;
import java.util.Optional;

import org.springframework.validation.Errors;

import com.web.blog.model.user.User;

public interface UserService {
	public User findUserByEmail(final String email);
	public Map<String, Object> User2Map(final User user);
	public User findUserById(int id);
	public Optional<User> create(final User user);
	public boolean update(final User user);
	public boolean delete(final String email);
	
	public boolean isValidPattern(final String password);
	public Map<String, String> getErrorMessage(final Errors errors);
}
