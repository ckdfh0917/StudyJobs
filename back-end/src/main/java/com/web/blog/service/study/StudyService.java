package com.web.blog.service.study;

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

public interface StudyService {
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
						final int numMeetings);
	
	public boolean delete(final int studyId);
	
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
						  final int numMeetings);

	public boolean isManager(final int studyId, final int userId);
	public Study findStudyByStudyId(final int studyId);
	public Map<String, Object> Study2Map(final Study study);
	public List<Map<String, Object>> findAllStudiesSimpleInfo();
	public Map<String, Object> findStudyDetailInfoByStudyId(final int studyId);
}
