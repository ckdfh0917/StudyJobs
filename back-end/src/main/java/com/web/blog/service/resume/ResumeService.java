package com.web.blog.service.resume;

import java.util.List;
import java.util.Map;

import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

public interface ResumeService {
    public Resume create(User user, String title, String company, String job, String category);

    public boolean create(Resume resume);

    public Resume read(int id);

    public List<Resume> readByTitle(String title);

    public boolean update(Resume resume);

    public boolean delete(int id);

    public List<Map<String, Object>> findAllResume();

    public Map<String, Object> Resume2Map(Resume resume);

    public List<Resume> findResumeByUser(int userId);

    public Resume findResumeById(int id);
}