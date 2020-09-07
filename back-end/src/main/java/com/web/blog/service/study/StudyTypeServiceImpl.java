package com.web.blog.service.study;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.blog.dao.study.CategoryDao;
import com.web.blog.dao.study.PeriodDao;
import com.web.blog.dao.study.PlaceDao;
import com.web.blog.dao.study.ShiftDao;
import com.web.blog.dao.study.WeekDao;
import com.web.blog.model.study.Category;
import com.web.blog.model.study.Period;
import com.web.blog.model.study.Place;
import com.web.blog.model.study.Shift;
import com.web.blog.model.study.Week;

@Service
@Transactional
public class StudyTypeServiceImpl implements StudyTypeService {
	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	PeriodDao periodDao;
	
	@Autowired
	PlaceDao placeDao;
	
	@Autowired
	ShiftDao shiftDao;
	
	@Autowired
	WeekDao weekDao;
	
	@Override
	public Category findCategoryById(int categoryId) {
		Optional<Category> categOpt = categoryDao.findCategoryByCategoryId(categoryId);
		return categOpt.isPresent() ? categOpt.get() : null;
	}

	@Override
	public Period findPeriodById(int periodId) {
		Optional<Period> periodOpt = periodDao.findPeriodByPeriodId(periodId);
		return periodOpt.isPresent() ? periodOpt.get() : null;
	}

	@Override
	public Place findPlaceById(int placeId) {
		Optional<Place> placeOpt = placeDao.findPlaceByPlaceId(placeId);
		return placeOpt.isPresent() ? placeOpt.get() : null;
	}

	@Override
	public Shift findShiftById(int shiftId) {
		Optional<Shift> shiftOpt = shiftDao.findShiftByShiftId(shiftId);
		return shiftOpt.isPresent() ? shiftOpt.get() : null;
	}

	@Override
	public Week findWeekById(int weekId) {
		Optional<Week> weekOpt = weekDao.findWeekByWeekId(weekId);
		return weekOpt.isPresent() ? weekOpt.get() : null;
	}
}
