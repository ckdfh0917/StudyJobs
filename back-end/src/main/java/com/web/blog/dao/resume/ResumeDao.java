package com.web.blog.dao.resume;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeDao extends JpaRepository<Resume, String> {
    Optional<Resume> findResumeById(int id);

    List<Resume> findResumeByUser(User user);

    Optional<Resume> findResumeByCompany(String company);

    Optional<Resume> findResumeByJob(String job);

    List<Resume> findByTitle(String title);
}