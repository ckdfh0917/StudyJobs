package com.web.blog.service.reply;

import java.util.List;
import java.util.Map;

import com.web.blog.model.reply.Reply;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

public interface ReplyService {
    public Reply create(User user, Resume resume, String content);

    public boolean create(Reply reply);

    public Map<String, Object> read(int id);

    public List<Map<String, Object>> findAllReply();

    public List<Map<String, Object>> readReplyByUser(int userId);

    public List<Map<String, Object>> readReplyByResume(int resumeId);

    public boolean update(Reply reply);

    public boolean delete(int id);

    public Map<String, Object> Reply2Map(Reply reply);

    public Reply read2Reply(int id);
}