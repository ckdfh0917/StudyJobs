package com.web.blog.dao.study;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Week;

@Repository
public interface WeekDao extends JpaRepository<Week, String>{
	Optional<Week> findWeekByWeekId(final int weekId);
}
