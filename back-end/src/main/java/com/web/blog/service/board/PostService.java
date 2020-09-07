package com.web.blog.service.board;

import java.util.List;
import java.util.Map;

import com.web.blog.model.board.Post;

public interface PostService {
    public Post findPostByTitle(String title);

    public boolean create(Post post);

    public boolean update(Post post);

    public boolean delete(int id);

    public Post findPostById(int id);

    public List<Post> findPostByStudy(int studyId);

    public List<Post> findAll();

    public Map<String, Object> Post2DetailInfo(Post post);

    public Map<String, Object> findPostDetailInfoByPostId(final int postId);
}