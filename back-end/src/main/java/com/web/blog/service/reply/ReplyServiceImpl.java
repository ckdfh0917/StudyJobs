package com.web.blog.service.reply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.web.blog.dao.reply.ReplyDao;
import com.web.blog.dao.resume.ResumeDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.reply.Reply;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public Reply create(User user, Resume resume, String content) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setResume(resume);
        reply.setUser(user);
        return replyDao.save(reply);
    }

    @Override
    public boolean create(Reply reply) {
        if (replyDao.findReplyById(reply.getId()).isPresent())
            return false;
        replyDao.save(reply);
        return true;
    }

    @Override
    public Map<String, Object> read(int id) {
        Optional<Reply> replyOpt = replyDao.findReplyById(id);
        return replyOpt.isPresent() ? Reply2Map(replyOpt.get()) : null;
    }

    @Override
    public List<Map<String, Object>> readReplyByUser(int userId) {
        Optional<User> userOpt = userDao.findUserById(userId);
        if (!userOpt.isPresent()) {
            return null;
        }
        User user = userOpt.get();
        List<Reply> list = replyDao.findReplyByUser(user);
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Reply reply : list) {
            listMap.add(Reply2Map(reply));
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> readReplyByResume(int resumeId) {
        Optional<Resume> resumeOpt = resumeDao.findResumeById(resumeId);
        if (!resumeOpt.isPresent()) {
            return null;
        }
        List<Reply> list = replyDao.findReplyByResume(resumeOpt.get());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Reply reply : list) {
            listMap.add(Reply2Map(reply));
        }
        return listMap;
    }

    @Override
    public boolean update(Reply reply) {
        Optional<Reply> replyOpt = replyDao.findReplyById(reply.getId());
        if (!replyOpt.isPresent()) {
            return false;
        }
        replyDao.save(reply);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Optional<Reply> replyOpt = replyDao.findReplyById(id);
        if (!replyOpt.isPresent()) {
            return false;
        }
        replyDao.delete(replyOpt.get());
        return true;
    }

    @Override
    public Map<String, Object> Reply2Map(Reply reply) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", reply.getId());
        ret.put("writerId", reply.getUser().getId());
        ret.put("writerEmail", reply.getUser().getEmail());
        ret.put("writerName", reply.getUser().getName());
        ret.put("content", reply.getContent());
        ret.put("resumeId", reply.getResume().getId());
        return ret;
    }

    @Override
    public List<Map<String, Object>> findAllReply() {
        List<Reply> list = replyDao.findAll();
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Reply reply : list) {
            listMap.add(Reply2Map(reply));
        }
        return listMap;
    }

    @Override
    public Reply read2Reply(int id) {
        Optional<Reply> replyOpt = replyDao.findReplyById(id);
        return replyOpt.isPresent() ? replyOpt.get() : null;
    }

}