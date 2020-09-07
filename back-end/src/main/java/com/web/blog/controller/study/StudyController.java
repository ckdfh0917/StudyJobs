package com.web.blog.controller.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.web.blog.model.address.Address;
import com.web.blog.model.study.Category;
import com.web.blog.model.study.Period;
import com.web.blog.model.study.Place;
import com.web.blog.model.study.Shift;
import com.web.blog.model.study.Study;
import com.web.blog.model.study.Week;
import com.web.blog.model.user.User;
import com.web.blog.service.address.AddressService;
import com.web.blog.service.auth.JwtService;
import com.web.blog.service.study.StudyMemberService;
import com.web.blog.service.study.StudyService;
import com.web.blog.service.study.StudyTypeService;
import com.web.blog.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
public class StudyController {
	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	JwtService jwtService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	StudyMemberService studyMemberService;

	@Autowired
	StudyTypeService studyTypeService;
	
	@GetMapping("/study/all")
	@ApiOperation(value = "모든 스터디의 리스트를 반환합니다.")
	public ResponseEntity findAll() {
		List<Map<String, Object>> res = studyService.findAllStudiesSimpleInfo();
		return new ResponseEntity(res, HttpStatus.OK);
	}

	@GetMapping("/study/{studyId}")
	@ApiOperation(value = "스터디 아이디를 입력받아 일치하는 스터디의 정보를 반환합니다.")
	public ResponseEntity read(@PathVariable final int studyId) {
		Map<String, Object> res = studyService.findStudyDetailInfoByStudyId(studyId);
		if(res==null) return new ResponseEntity("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity(res, HttpStatus.OK);
	}

	@PostMapping("/study")
	@ApiOperation(value = "스터디 데이터를 입력받아 데이터를 검증하고 새로운 스터디를 생성합니다.")
	public ResponseEntity create(@RequestHeader(value="jwt-auth-token") final String token,
								 @RequestParam final String title,
								 @RequestParam final String content,
								 @DateTimeFormat(iso = ISO.DATE) @RequestParam final LocalDate startDate,
								 @DateTimeFormat(iso = ISO.DATE) @RequestParam final LocalDate endDate,
								 @RequestParam final int maxMembers,
								 @RequestParam final String si,
								 @RequestParam final String gu,
								 @RequestParam final int categoryId,
								 @RequestParam final int placeId,
								 @RequestParam final int periodId,
								 @RequestParam final int shiftId,
								 @RequestParam final int weekId,
								 @RequestParam final int numMeetings) {
		final String email = jwtService.parseEmail(token);
		final User user = userService.findUserByEmail(email);
		if (user == null) return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);

		final Address address = addressService.findAddressBySiAndGu(si, gu);
		if (address == null) return new ResponseEntity("존재하지 않는 주소입니다.", HttpStatus.NOT_FOUND);

		if (endDate.compareTo(startDate) < 0)
			return new ResponseEntity("종료일은 시작일 보다 빠를수 없습니다", HttpStatus.FORBIDDEN);
		
		Category category = studyTypeService.findCategoryById(categoryId);
		if (category == null) return new ResponseEntity("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND);
		
		Period period = studyTypeService.findPeriodById(periodId);
		if (period == null) return new ResponseEntity("존재하지 않는 기간입니다.", HttpStatus.NOT_FOUND);

		Place place = studyTypeService.findPlaceById(placeId);
		if (place == null) return new ResponseEntity("존재하지 않는 장소입니다.", HttpStatus.NOT_FOUND);

		Shift shift = studyTypeService.findShiftById(shiftId);
		if (shift == null) return new ResponseEntity("존재하지 않는 시간대입니다.", HttpStatus.NOT_FOUND);
		
		Week week = studyTypeService.findWeekById(weekId);
		if (week == null) return new ResponseEntity("존재하지 않는 요일입니다.", HttpStatus.NOT_FOUND);

		Study study = studyService.create(user, address, title, content, startDate, endDate, maxMembers, category, period, place, shift, week, numMeetings);
		if(study == null) return new ResponseEntity("스터디를 생성할 수 없습니다. 관리자에게 문의하세요.", HttpStatus.FORBIDDEN);
		
		return new ResponseEntity("스터디 생성에 성공했습니다.", HttpStatus.OK);
	}

	@DeleteMapping("/study")
	@ApiOperation(value = "스터디 아이디를 입력받아 존재 여부를 확인하고, 해당 스터디를 삭제합니다.")
	public ResponseEntity delete(@RequestHeader(value="jwt-auth-token") final String token, @RequestParam final int studyId) {
		final String email = jwtService.parseEmail(token);
		final User user = userService.findUserByEmail(email);
		if(user==null)
			return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		else if(studyService.isManager(studyId, user.getId())==false)
			return new ResponseEntity("스터디 삭제 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
		else if(studyService.delete(studyId)==false)
			return new ResponseEntity("해당 스터디가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		return new ResponseEntity("스터디 삭제에 성공했습니다.", HttpStatus.OK);
	}

	@PutMapping("/study")
	@ApiOperation(value = "스터디 정보를 입력받아 데이터를 검증하고, 해당 스터디의 정보를 수정합니다.")
	public ResponseEntity update(@RequestHeader(value="jwt-auth-token") final String token,
								 @RequestParam final int studyId,
								 @RequestParam final String title,
								 @RequestParam final String content,
								 @DateTimeFormat(iso = ISO.DATE) @RequestParam final LocalDate startDate,
								 @DateTimeFormat(iso = ISO.DATE) @RequestParam final LocalDate endDate,
								 @RequestParam final int maxMembers,
								 @RequestParam final String si,
								 @RequestParam final String gu,
								 @RequestParam final int categoryId,
								 @RequestParam final int placeId,
								 @RequestParam final int periodId,
								 @RequestParam final int shiftId,
								 @RequestParam final int weekId,
								 @RequestParam final int numMeetings) {
		final String email = jwtService.parseEmail(token);
		final User user = userService.findUserByEmail(email);
		if(user==null) return new ResponseEntity("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
		
		final Study study = studyService.findStudyByStudyId(studyId);
		if(study==null)	return new ResponseEntity("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND);
		else if(studyService.isManager(studyId, user.getId())==false)
			return new ResponseEntity("스터디 수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
		
		final Address address = addressService.findAddressBySiAndGu(si, gu);
		if (address == null) return new ResponseEntity("존재하지 않는 주소입니다.", HttpStatus.NOT_FOUND);

		if (endDate.compareTo(startDate) < 0)
			return new ResponseEntity("종료일은 시작일 보다 빠를수 없습니다", HttpStatus.FORBIDDEN);

		Category category = studyTypeService.findCategoryById(categoryId);
		if (category == null) return new ResponseEntity("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND);
		
		Period period = studyTypeService.findPeriodById(periodId);
		if (period == null) return new ResponseEntity("존재하지 않는 기간입니다.", HttpStatus.NOT_FOUND);

		Place place = studyTypeService.findPlaceById(placeId);
		if (place == null) return new ResponseEntity("존재하지 않는 장소입니다.", HttpStatus.NOT_FOUND);

		Shift shift = studyTypeService.findShiftById(shiftId);
		if (shift == null) return new ResponseEntity("존재하지 않는 시간대입니다.", HttpStatus.NOT_FOUND);
		
		Week week = studyTypeService.findWeekById(weekId);
		if (week == null) return new ResponseEntity("존재하지 않는 요일입니다.", HttpStatus.NOT_FOUND);

		if(studyService.update(address, studyId, title, content, startDate, endDate, maxMembers, category, period, place, shift, week, numMeetings)==false)
			return new ResponseEntity("스터디를 수정할 수 없습니다. 관리자에게 문의바랍니다.", HttpStatus.FORBIDDEN);
		return new ResponseEntity("스터디 수정에 성공했습니다.", HttpStatus.OK);
	}
}