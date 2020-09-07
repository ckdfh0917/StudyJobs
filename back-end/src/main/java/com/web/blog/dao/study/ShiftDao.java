package com.web.blog.dao.study;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Shift;

@Repository
public interface ShiftDao extends JpaRepository<Shift, String>{
	Optional<Shift> findShiftByShiftId(final int shiftId);
}
