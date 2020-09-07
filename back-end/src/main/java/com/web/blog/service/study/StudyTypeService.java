package com.web.blog.service.study;

import com.web.blog.model.study.Category;
import com.web.blog.model.study.Period;
import com.web.blog.model.study.Place;
import com.web.blog.model.study.Shift;
import com.web.blog.model.study.Week;

public interface StudyTypeService {
	public Category findCategoryById(final int categoryId);
	public Period findPeriodById(final int periodId);
	public Place findPlaceById(final int placeId);
	public Shift findShiftById(final int shiftId);
	public Week findWeekById(final int weekId);
}
