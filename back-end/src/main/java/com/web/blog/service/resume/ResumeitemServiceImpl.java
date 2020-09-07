package com.web.blog.service.resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.web.blog.dao.resume.ResumeDao;
import com.web.blog.dao.resume.ResumeitemDao;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.resume.Resumeitem;
import com.web.blog.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResumeitemServiceImpl implements ResumeitemService {
    @Autowired
    private ResumeitemDao resumeitemDao;
    @Autowired
    private ResumeDao resumeDao;

    @Override
    public Resumeitem create(Resume resume, String title, String content) {
        Resumeitem resumeitem = new Resumeitem();
        resumeitem.setResume(resume);
        resumeitem.setContent(content);
        resumeitem.setTitle(title);
        return resumeitemDao.save(resumeitem);
    }

    @Override
    public Resumeitem read(int id) {
        Optional<Resumeitem> resumeitemOpt = resumeitemDao.findResumeitemById(id);
        return resumeitemOpt.isPresent() ? resumeitemOpt.get() : null;
    }

    @Override
    public List<Resumeitem> readByResume(int resumeId) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(resumeId);
        if (!resumeOpt.isPresent()) {
            return null;
        }
        Resume resume = resumeOpt.get();
        List<Resumeitem> list = resumeitemDao.findResumeitemByResume(resume);
        return list;
    }

    @Override
    public boolean update(Resumeitem resumeitem) {
        Optional<Resumeitem> resumeitemOpt = resumeitemDao.findResumeitemById(resumeitem.getId());
        if (!resumeitemOpt.isPresent()) {
            return false;
        }
        resumeitemDao.save(resumeitem);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Optional<Resumeitem> resumeitemOpt = resumeitemDao.findResumeitemById(id);
        if (!resumeitemOpt.isPresent()) {
            return false;
        }
        resumeitemDao.delete(resumeitemOpt.get());
        return true;
    }

    @Override
    public List<Map<String, Object>> findAllResumeitem() {
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Resumeitem resumeitem : resumeitemDao.findAll())
            ret.add(Resumeitem2Map(resumeitem));
        return ret;
    }

    @Override
    public Map<String, Object> Resumeitem2Map(Resumeitem resumeitem) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("title", resumeitem.getTitle());
        ret.put("resumeId", resumeitem.getResume().getId());
        ret.put("name", resumeitem.getResume().getUser().getName());
        ret.put("content", resumeitem.getContent());
        return ret;
    }

    @Override
    public boolean create(Resumeitem resumeitem) {
        if (resumeitemDao.findResumeitemById(resumeitem.getId()).isPresent()) {
            return false;
        }
        resumeitemDao.save(resumeitem);
        return true;
    }

    @Override
    public String findUserByResumeitem(int resumeitemId) {
        return resumeitemDao.findUserEmailByResumeitemId(resumeitemId);
    }

    @Override
    public void create(List<Resumeitem> list) {
        resumeitemDao.saveAll(list);
    }

}