package com.web.blog.service.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.web.blog.dao.user.UserDao;
import com.web.blog.model.user.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	static final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$";

	@Override
	public User findUserByEmail(final String email) {
		Optional<User> userOpt = userDao.findUserByEmail(email);
		return userOpt.isPresent() ? userOpt.get() : null;
	}

	@Override
	public User findUserById(int id) {
		Optional<User> userOpt = userDao.findUserById(id);
		return userOpt.isPresent() ? userOpt.get() : null;
	}

	@Override
	public Map<String, Object> User2Map(User user) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("id", user.getId());
		ret.put("email", user.getEmail());
		ret.put("name", user.getName());
		ret.put("age", user.getAge());
		ret.put("sex", user.getSex());
		return ret;
	}

	@Override
	public Map<String, String> getErrorMessage(final Errors errors) {
		Map<String, String> ret = new HashMap<>();
		for (FieldError error : errors.getFieldErrors())
			ret.put(error.getField(), error.getDefaultMessage());
		return ret;
	}

	@Override
	public Optional<User> create(final User user) {
	
		if (userDao.findUserByEmail(user.getEmail()).isPresent())
			return Optional.empty();

		final String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setFireBasePassword(encodedPassword.substring(0,24));
		return Optional.ofNullable(userDao.save(user));
	}

	@Override
	public boolean update(final User user) {
		Optional<User> userOpt = userDao.findUserByEmail(user.getEmail());
		if (userOpt.isPresent() == false)
			return false;

		final String encodedPassword = passwordEncoder.encode(user.getPassword());

		userOpt.ifPresent(u -> {
			u.setPassword(encodedPassword);
			u.setName(user.getName());
			u.setAge(user.getAge());
			u.setSex(user.getSex());
			userDao.save(u);
		});

		return true;
	}

	@Override
	public boolean delete(final String email) {
		Optional<User> userOpt = userDao.findUserByEmail(email);
		if (userOpt.isPresent() == false)
			return false;

		userOpt.ifPresent(user -> {
			userDao.delete(user);
		});
		return true;
	}

	@Override
	public boolean isValidPattern(String password) {
		return Pattern.matches(regex, password);
	}
}
