package com.web.blog.service.resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.web.blog.dao.resume.ResumeDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeDao resumeDao;
    @Autowired
    private UserDao userDao;

    @Override
    public boolean create(Resume resume) {
        if (resumeDao.findResumeById(resume.getId()).isPresent()) {
            return false;
        }
        resumeDao.save(resume);
        return true;
    }

    @Override
    public Resume read(int id) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(id);
        return resumeOpt.isPresent() ? resumeOpt.get() : null;
    }

    @Override
    public List<Resume> readByTitle(String title) {
        return resumeDao.findByTitle(title);
    }

    @Override
    public boolean update(Resume resume) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(resume.getId());
        if (!resumeOpt.isPresent()) {
            return false;
        }
        resumeOpt.ifPresent(r -> {
            r.setCompany(resume.getCompany());
            r.setJob(resume.getJob());
            r.setTitle(resume.getTitle());
            r.setUser(resume.getUser());
            resumeDao.save(r);
        });
        return true;
    }

    @Override
    public boolean delete(int id) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(id);
        if (!resumeOpt.isPresent())
            return false;

        resumeOpt.ifPresent(resume -> {
            resumeDao.delete(resume);
        });
        return true;
    }

    @Override
    public Map<String, Object> Resume2Map(Resume resume) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("company", resume.getCompany());
        ret.put("id", resume.getId());
        ret.put("job", resume.getJob());
        ret.put("title", resume.getTitle());
        ret.put("userId", resume.getUser().getId());
        ret.put("email", resume.getUser().getEmail());
        ret.put("category", resume.getCategory());
        ret.put("name", resume.getUser().getName());

        return ret;
    }

    @Override
    public List<Map<String, Object>> findAllResume() {
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Resume resume : resumeDao.findAll())
            ret.add(Resume2Map(resume));
        return ret;
    }

    @Override
    public Resume create(User user, String title, String company, String job, String category) {
        Resume resume = new Resume();
        resume.setUser(user);
        resume.setJob(job);
        resume.setTitle(title);
        resume.setCompany(company);
        resume.setCategory(category);
        return resumeDao.save(resume);
    }

    @Override
    public List<Resume> findResumeByUser(int userId) {
        Optional<User> userOpt = userDao.findUserById(userId);
        if (!userOpt.isPresent()) {
            return null;
        }
        User user = userOpt.get();
        List<Resume> list = resumeDao.findResumeByUser(user);
        return list;
    }

    @Override
    public Resume findResumeById(int id) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(id);
        return resumeOpt.isPresent() ? resumeOpt.get() : null;
    }

}