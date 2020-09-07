package com.web.blog.controller.resume;

import java.util.List;
import java.util.Map;

import com.web.blog.model.resume.Resume;
import com.web.blog.model.resume.Resumeitem;
import com.web.blog.service.resume.ResumeService;
import com.web.blog.service.resume.ResumeitemService;

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
import io.swagger.models.Response;

@RestController
public class ResumeitemController {
    @Autowired
    ResumeitemService resumeitemService;

    @Autowired
    ResumeService resumeService;

    @GetMapping("/resumeitem/all")
    @ApiOperation(value = "")
    public ResponseEntity<Object> findAll() {
        List<Map<String, Object>> res = resumeitemService.findAllResumeitem();
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/resumeitem/{resumeitemId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> read(@PathVariable int resumeitemId) {
        Map<String, Object> res = resumeitemService.Resumeitem2Map(resumeitemService.read(resumeitemId));
        if (res == null) {
            return new ResponseEntity<Object>("존재하지 않는 항목입니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/resumeitem/resume/{resumeId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> readByResume(@PathVariable int resumeId) {
        List<Resumeitem> list = resumeitemService.readByResume(resumeId);
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }

    @PostMapping("/resumeitem")
    @ApiOperation(value = "")
    public ResponseEntity<Object> create(@RequestParam String title, @RequestParam String content,
            @RequestParam int resumeId) {
        Resume resume = resumeService.findResumeById(resumeId);
        if (resume == null) {
            return new ResponseEntity<Object>("존재하지 않는 자소서입니다.", HttpStatus.NOT_FOUND);
        }
        Resumeitem resumeitem = resumeitemService.create(resume, title, content);
        if (resumeitem == null) {
            return new ResponseEntity<Object>("항목을 생성할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Object>("생성에 성공했습니다.", HttpStatus.OK);

    }

    @DeleteMapping("/resumeitem")
    @ApiOperation(value = "")
    public ResponseEntity<Object> delete(int resumeitemId) {
        if (!resumeitemService.delete(resumeitemId)) {
            return new ResponseEntity<Object>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("삭제에 성공했습니다.", HttpStatus.OK);
    }

    @PutMapping("/resumeitem")
    @ApiOperation(value = "")
    public ResponseEntity<Object> update(@RequestParam String title, @RequestParam String content,
            @RequestParam int resumeitemId) {
        Resumeitem resumeitem = resumeitemService.read(resumeitemId);
        if (resumeitem == null) {
            return new ResponseEntity<Object>("존재하지 않는 항목입니다.", HttpStatus.NOT_FOUND);
        }
        resumeitem.setContent(content);
        resumeitem.setTitle(title);
        if (!resumeitemService.update(resumeitem)) {
            return new ResponseEntity<Object>("업데이트에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("업데이트에 성공하였습니다.", HttpStatus.OK);
    }

    @GetMapping("/resumeitem/email/{resumeitemId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> findEmailbyResumeitemId(@PathVariable int resumeitemId) {
        String email = resumeitemService.findUserByResumeitem(resumeitemId);
        return new ResponseEntity<Object>(email, HttpStatus.OK);
    }
}