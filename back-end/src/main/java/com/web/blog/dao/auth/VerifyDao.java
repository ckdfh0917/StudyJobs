package com.web.blog.dao.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.blog.model.auth.Verify;

@Repository
public interface VerifyDao extends JpaRepository<Verify, String>{
	public Optional<Verify> findVerifyByEmailAndCode(String email, String code);
}
