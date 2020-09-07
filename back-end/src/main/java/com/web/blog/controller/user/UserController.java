package com.web.blog.controller.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.web.blog.model.user.User;
import com.web.blog.service.auth.VerifyService;
import com.web.blog.service.user.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	VerifyService verifyService;

	@PostMapping("/user/signUp")
	@ApiOperation(value = "사용자 정보와 이메일 인증 번호를 입력받아 유효성을 검증하고, 결과에 따라 회원가입을 승인/거부합니다.")
	public ResponseEntity create(@RequestBody @Valid final User user, Errors errors, @RequestParam final String code) {
		if (errors.hasErrors())
			return new ResponseEntity(userService.getErrorMessage(errors), HttpStatus.BAD_REQUEST);
		else if (verifyService.isValidCode(user.getEmail(), code) == false)
			return new ResponseEntity("인증 번호가 유효하지 않습니다.", HttpStatus.NOT_FOUND);
		else if (userService.isValidPattern(user.getPassword()) == false)
			return new ResponseEntity("비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함된 8자이상으로 구성되어야 합니다.", HttpStatus.BAD_REQUEST);
		Optional<User> optUser = userService.create(user);
		if(optUser.isPresent()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("msg", "회원가입이 완료되었습니다.");
			map.put("fbPassword",optUser.get().getFireBasePassword());
			return new ResponseEntity(map,HttpStatus.OK);
		}
		else return new ResponseEntity("이미 사용중인 이메일 입니다.", HttpStatus.CONFLICT); // Double Ch
		
		
	}

	@GetMapping("/user/{email}")
	@ApiOperation(value = "이메일을 입력받아 일치하는 사용자의 정보를 반환합니다.")
	public ResponseEntity read(@PathVariable final String email) {
		User user = userService.findUserByEmail(email);
		if (user == null)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity(userService.User2Map(user), HttpStatus.OK);
	}

	@GetMapping("/user/id/{id}")
	@ApiOperation(value = "아이디르 입력받아 일치하는 사용자의 정보를 반환합니다.")
	public ResponseEntity readId(@PathVariable final int id) {
		User user = userService.findUserById(id);
		if (user == null)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity(userService.User2Map(user), HttpStatus.OK);
	}

	@PutMapping("/user")
	@ApiOperation(value = "사용자 정보를 입력받아 정보를 수정하고 데이터베이스에 반영합니다.")
	public ResponseEntity update(@Valid @RequestBody final User user, Errors errors) {
		if (errors.hasErrors())
			return new ResponseEntity(userService.getErrorMessage(errors), HttpStatus.BAD_REQUEST);
		else if (userService.isValidPattern(user.getPassword()) == false)
			return new ResponseEntity("비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함된 8자이상으로 구성되어야 합니다.", HttpStatus.BAD_REQUEST);
		else if (userService.update(user) == false)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity("회원정보 수정을 완료하였습니다.", HttpStatus.OK);
	}

	@DeleteMapping("/user")
	@ApiOperation(value = "이메일과 비밀번호를 입력받아 유효성을 검증하고, 결과에 따라 회원탈퇴를 승인/거부합니다.")
	public ResponseEntity delete(@RequestParam final String email, @RequestParam final String password) {
		if (verifyService.isValidUser(email, password) == false)
			return new ResponseEntity("이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND);
		else if (userService.delete(email) == false)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity("회원탈퇴가 완료되었습니다.", HttpStatus.OK);
	}

	@PutMapping("/user/help")
	@ApiOperation(value = "이메일, 인증 번호 그리고 변경할 패스워드를 입력받아 유효성을 재검증하고, 결과에 따라 비밀번호 변경을 승인/거부합니다.")
	public ResponseEntity updatePassword(@RequestParam final String email, @RequestParam final String code,
			@RequestParam final String password) {
		if (userService.isValidPattern(password) == false)
			return new ResponseEntity("비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함된 8자이상으로 구성되어야 합니다.", HttpStatus.BAD_REQUEST);
		else if (verifyService.isValidCode(email, code) == false)
			return new ResponseEntity("인증번호가 유효하지 않습니다.", HttpStatus.FORBIDDEN);

		User user = userService.findUserByEmail(email);
		if (user == null)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);

		user.setPassword(password);
		if (userService.update(user) == false)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity("비밀번호가 변경되었습니다.", HttpStatus.OK);
	}
}