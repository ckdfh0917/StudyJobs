package com.web.blog.dao.study;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Period;

@Repository
public interface PeriodDao extends JpaRepository<Period, String>{
	Optional<Period> findPeriodByPeriodId(final int periodId);
}
