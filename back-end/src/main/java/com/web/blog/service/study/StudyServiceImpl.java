package com.web.blog.service.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.blog.dao.study.CategoryDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.model.address.Address;
import com.web.blog.model.study.Category;
import com.web.blog.model.study.Period;
import com.web.blog.model.study.Place;
import com.web.blog.model.study.Shift;
import com.web.blog.model.study.Study;
import com.web.blog.model.study.Week;
import com.web.blog.model.user.User;

@Service
@Transactional
public class StudyServiceImpl implements StudyService {
	@Autowired
	StudyDao studyDao;

	@Override
	public Study create(final User user,
						final Address address,
						final String title,
						final String content,
						final LocalDate startDate,
						final LocalDate endDate,
						final int maxMembers,
						final Category category,
						final Period period,
						final Place place,
						final Shift shift,
						final Week week,
						final int numMeetings) {
		Study study = new Study();
		study.setUser(user);
		study.setAddress(address);
		study.setTitle(title);
		study.setContent(content);
		study.setStartDate(startDate);
		study.setEndDate(endDate);
		study.setMaxMembers(maxMembers);
		study.setCategory(category);
		study.setPeriod(period);
		study.setPlace(place);
		study.setShift(shift);
		study.setWeek(week);
		study.setNumMeetings(numMeetings);
		Set <User> set = new HashSet<User>();
		set.add(user);
		study.setMembers(set);
		return studyDao.save(study);
	}

	@Override
	public boolean delete(final int studyId) {
		Optional<Study> studyOpt = studyDao.findStudyByStudyId(studyId);
		if(studyOpt.isPresent()==false) return false;
		studyOpt.ifPresent(study->{
			studyDao.delete(study);
		}); return true;
	}
	
	@Override
	public boolean update(final Address address,
						  final int studyId,
						  final String title,
						  final String content,
						  final LocalDate startDate,
						  final LocalDate endDate,
						  final int maxMembers,
						  final Category category,
						  final Period period,
						  final Place place,
						  final Shift shift,
						  final Week week,
						  final int numMeetings) {
		Optional<Study> studyOpt = studyDao.findStudyByStudyId(studyId);
		if(studyOpt.isPresent()==false) return false;
		studyOpt.ifPresent(study->{
			study.setAddress(address);
			study.setTitle(title);
			study.setContent(content);
			study.setStartDate(startDate);
			study.setEndDate(endDate);
			study.setMaxMembers(maxMembers);
			study.setCategory(category);
			study.setPeriod(period);
			study.setPlace(place);
			study.setShift(shift);
			study.setWeek(week);
			study.setNumMeetings(numMeetings);
			studyDao.save(study);
		}); return true;
	}
	
	@Override
	public boolean isManager(final int studyId, final int userId) {
		Optional<Study> studyOpt = studyDao.findStudyByStudyId(studyId);
		return studyOpt.isPresent() ? studyOpt.get().getUser().getId()==userId : false;
	}
	
	@Override
	public Study findStudyByStudyId(final int studyId) {
		Optional<Study> studyOpt = studyDao.findStudyByStudyId(studyId);
		return studyOpt.isPresent() ? studyOpt.get() : null;
	}

	@Override
	public Map<String, Object> Study2Map(Study study){
		Map<String, Object> ret = new HashMap<>();
		ret.put("studyId", study.getStudyId());
		ret.put("title", study.getTitle());
		ret.put("content", study.getContent());
		ret.put("mgrEmail", study.getUser().getEmail());
		ret.put("mgrName", study.getUser().getName());
		ret.put("numMembers", study.getMembers().size());
		ret.put("maxMembers", study.getMaxMembers());
		ret.put("startDate", study.getStartDate());
		ret.put("endDate", study.getEndDate());
		ret.put("si", study.getAddress().getSi());
		ret.put("gu", study.getAddress().getGu());
		ret.put("category", study.getCategory().getName());
		ret.put("period", study.getPeriod().getName());
		ret.put("place", study.getPlace().getName());
		ret.put("shift", study.getShift().getName());
		ret.put("week", study.getWeek().getName());
		ret.put("numMeetings", study.getNumMeetings());
		return ret;
	}
	
	@Override
	public List<Map<String, Object>> findAllStudiesSimpleInfo() {
		List<Map<String, Object>> ret = new ArrayList<>();
		for(Study study : studyDao.findAll())
			ret.add(Study2Map(study));
		return ret;
	}

	@Override
	public Map<String, Object> findStudyDetailInfoByStudyId(final int studyId) {
		Study study = findStudyByStudyId(studyId);
		return study==null ? null : Study2Map(study);
	}
}
