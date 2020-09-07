package com.web.blog.service.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.web.blog.dao.board.PostDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.model.board.Post;
import com.web.blog.model.study.Study;
import com.web.blog.service.study.StudyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostDao postDao;

    @Autowired
    StudyDao studyDao;

    @Override
    public List<Post> findPostByStudy(int studyId) {
        Study study = studyDao.findStudyByStudyId(studyId).get();
        return study == null ? null : study.getPostList();
    }

    @Override
    public List<Post> findAll() {
        List<Post> list = postDao.findAll();
        return list;
    }

    @Override
    public Post findPostByTitle(final String title) {
        Optional<Post> postOpt = postDao.findPostByTitle(title);
        return postOpt.isPresent() ? postOpt.get() : null;
    }

    @Override
    public boolean create(Post post) {
        if (postDao.findPostById(post.getId()).isPresent())
            return false;
        postDao.save(post);
        return true;
    }

    @Override
    public boolean update(Post post) {
        Optional<Post> postOpt = postDao.findPostById(post.getId());
        if (postOpt.isPresent() == false)
            return false;

        postOpt.ifPresent(p -> {
            p.setContent(post.getContent());
            p.setStudy(post.getStudy());
            p.setTitle(post.getTitle());
            p.setUser(post.getUser());
            postDao.save(p);
        });

        return true;
    }

    @Override
    public boolean delete(int id) {
        Optional<Post> postOpt = postDao.findPostById(id);
        if (postOpt.isPresent() == false)
            return false;

        postOpt.ifPresent(post -> {
            postDao.delete(post);
        });
        return true;
    }

    @Override
    public Post findPostById(int id) {
        Optional<Post> postOpt = postDao.findPostById(id);
        return postOpt.isPresent() ? postOpt.get() : null;
    }

    @Override
    public Map<String, Object> Post2DetailInfo(Post post) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", post.getId());
        ret.put("content", post.getContent());
        ret.put("study_id", post.getStudy().getStudyId());
        ret.put("title", post.getTitle());
        ret.put("writer", post.getUser().getId());
        ret.put("date", post.getDate());
        return ret;
    }

    @Override
    public Map<String, Object> findPostDetailInfoByPostId(final int postId) {
        Post post = findPostById(postId);
        return post == null ? null : Post2DetailInfo(post);
    }

}