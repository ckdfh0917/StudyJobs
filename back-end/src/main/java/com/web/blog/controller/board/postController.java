package com.web.blog.controller.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.web.blog.model.board.Post;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;
import com.web.blog.service.board.PostService;
import com.web.blog.service.study.StudyService;
import com.web.blog.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class postController {
    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    StudyService studyService;

    @PostMapping("/post")
    @ApiOperation(value = "")
    public ResponseEntity<Object> create(@RequestParam int studyId, @RequestParam int writer,
            @RequestParam String content, @RequestParam String title) {
        Study study = studyService.findStudyByStudyId(studyId);
        if (study == null) {
            return new ResponseEntity<Object>("존재하지 않는 스터디 입니다.", HttpStatus.NOT_FOUND);
        }
        User user = userService.findUserById(writer);
        if (user == null) {
            return new ResponseEntity<Object>("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
        }
        Post post = new Post();
        post.setStudy(study);
        post.setUser(user);
        post.setTitle(title);
        post.setContent(content);
        if (postService.create(post) == false) {
            return new ResponseEntity<Object>("이미 존재하는 글입니다.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Object>("생성에 성공하였습니다.", HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> read(@PathVariable int id) {
        Map<String, Object> res = postService.findPostDetailInfoByPostId(id);
        if (res == null)
            return new ResponseEntity<Object>("존재하지 않는 글입니다.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/post/study/{studyId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> readAllByStudyId(@PathVariable int studyId) {
        List<Post> list = postService.findPostByStudy(studyId);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Post tmp : list) {
            res.add(postService.Post2DetailInfo(tmp));
        }
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/post/all")
    @ApiOperation(value = "")
    public ResponseEntity<Object> readAll() {
        List<Post> list = postService.findAll();
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }

    @PutMapping("/post")
    @ApiOperation(value = "")
    public ResponseEntity<Object> update(@RequestParam int id, @RequestParam String content,
            @RequestParam String title) {
        Post post = postService.findPostById(id);
        if (post == null) {
            return new ResponseEntity<Object>("존재하지 않는 글입니다.", HttpStatus.NOT_FOUND);
        }
        post.setTitle(title);
        post.setContent(content);
        if (postService.update(post) == false) {
            return new ResponseEntity<Object>("존재하지 않는 글입니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>("글 정보를 수정하였습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/post")
    @ApiOperation(value = "")
    public ResponseEntity<Object> delete(@RequestParam int id) {
        if (postService.delete(id) == false) {
            return new ResponseEntity<Object>("존재하지 않는 글입니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>("삭제에 성공하였습니다.", HttpStatus.OK);
    }
}