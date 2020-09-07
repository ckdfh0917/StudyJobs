package com.web.blog.dao.board;

import java.util.List;
import java.util.Optional;

import com.web.blog.model.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDao extends JpaRepository<Post, String> {
    Optional<Post> findPostByTitle(String title);

    Optional<Post> findPostById(int id);

    List<Post> findPostByStudy(int studyId);
}