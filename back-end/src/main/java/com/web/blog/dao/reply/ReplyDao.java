package com.web.blog.dao.reply;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.reply.Reply;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyDao extends JpaRepository<Reply, String> {
    Optional<Reply> findReplyById(int id);

    List<Reply> findReplyByUser(User user);

    List<Reply> findReplyByResume(Resume resume);

}