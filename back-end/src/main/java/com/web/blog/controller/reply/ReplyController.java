package com.web.blog.controller.reply;

import java.util.List;
import java.util.Map;

import com.web.blog.model.reply.Reply;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;
import com.web.blog.service.reply.ReplyService;
import com.web.blog.service.resume.ResumeService;
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
public class ReplyController {
    @Autowired
    ReplyService replyService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    UserService userService;

    @GetMapping("/reply/all")
    @ApiOperation(value = "")
    public ResponseEntity<Object> findAll() {
        List<Map<String, Object>> res = replyService.findAllReply();
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/reply/{replyId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> read(@PathVariable int replyId) {
        Map<String, Object> res = replyService.read(replyId);
        if (res == null) {
            return new ResponseEntity<Object>("존재하지 않는 항목입니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/reply/resume/{resumeId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> readByResume(@PathVariable int resumeId) {
        List<Map<String, Object>> list = replyService.readReplyByResume(resumeId);
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }

    @PostMapping("/reply")
    @ApiOperation(value = "")
    public ResponseEntity<Object> create(@RequestParam String content, @RequestParam int userId,
            @RequestParam int resumeId) {
        Resume resume = resumeService.findResumeById(resumeId);
        if (resume == null) {
            return new ResponseEntity<Object>("존재하지 않는 자소서입니다.", HttpStatus.NOT_FOUND);
        }
        User user = userService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity<Object>("존재하지 않는 작성자입니다.", HttpStatus.NOT_FOUND);
        }
        Reply reply = replyService.create(user, resume, content);
        if (reply == null) {
            return new ResponseEntity<Object>("댓글을 생성할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Object>("생성에 성공했습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/reply")
    @ApiOperation(value = "")
    public ResponseEntity<Object> delete(int replyId) {
        if (!replyService.delete(replyId)) {
            return new ResponseEntity<Object>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("삭제에 성공했습니다.", HttpStatus.OK);
    }

    @PutMapping("/reply")
    @ApiOperation(value = "")
    public ResponseEntity<Object> update(@RequestParam String content, @RequestParam int replyId) {
        Reply reply = replyService.read2Reply(replyId);
        if (reply == null) {
            return new ResponseEntity<Object>("존재하지 않는 항목입니다.", HttpStatus.NOT_FOUND);
        }
        reply.setContent(content);
        if (!replyService.update(reply)) {
            return new ResponseEntity<Object>("업데이트에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("업데이트에 성공하였습니다.", HttpStatus.OK);
    }
}