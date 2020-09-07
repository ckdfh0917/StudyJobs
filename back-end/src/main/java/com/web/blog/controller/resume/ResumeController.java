package com.web.blog.controller.resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.resume.Resumeitem;
import com.web.blog.model.user.User;
import com.web.blog.service.auth.JwtService;
import com.web.blog.service.resume.ResumeService;
import com.web.blog.service.resume.ResumeitemService;
import com.web.blog.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@Transactional
public class ResumeController {
    @Autowired
    ResumeService resumeService;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    ResumeitemService resumeitemService;

    @GetMapping("/resume/all")
    @ApiOperation(value = "")
    public ResponseEntity<Object> findAll() {
        List<Map<String, Object>> res = resumeService.findAllResume();
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/resume/{resumeId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> read(@PathVariable int resumeId) {
        Map<String, Object> res = resumeService.Resume2Map(resumeService.read(resumeId));
        if (res == null) {
            return new ResponseEntity<Object>("존재하지 않는 자소서입니다.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(res, HttpStatus.OK);
    }

    @GetMapping("/resume/user/{userId}")
    @ApiOperation(value = "")
    public ResponseEntity<Object> readByUser(@PathVariable int userId) {
        List<Resume> list = resumeService.findResumeByUser(userId);
        return new ResponseEntity<Object>(list, HttpStatus.OK);
    }

    @PostMapping("/resume")
    @ApiOperation(value = "")
    public ResponseEntity<Object> create(@RequestParam String title, @RequestParam String email,
            @RequestParam String company, @RequestParam String job, @RequestParam String category) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<Object>("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
        }
        Resume resume = resumeService.create(user, title, company, job, category);
        if (resume == null) {
            return new ResponseEntity<Object>("자소서를 생성할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Object>(resume.getId(), HttpStatus.OK);
    }

    @PostMapping("/resume/resumeandresumeitem")
    @ApiOperation(value = "")
    public ResponseEntity<Object> createResumeAndResumeitem(@RequestBody Map<String, Object> resume,
            @RequestHeader(value = "jwt-auth-token") final String token) {
        final String email = jwtService.parseEmail(token);
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<Object>("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
        }
        Resume res = resumeService.create(user, (String) resume.get("title"), (String) resume.get("company"),
                (String) resume.get("job"), (String) resume.get("category"));
        if (res == null) {
            return new ResponseEntity<Object>("자소서를 생성할 수 없습니다.", HttpStatus.FORBIDDEN);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<Resumeitem> arr = mapper.convertValue(resume.get("resumeItems"), new TypeReference<List<Resumeitem>>() {
        });
        for(Resumeitem r : arr) {
            r.setResume(res);
            System.out.println(r.getTitle());
            System.out.println(r.getContent());
         }
        resumeitemService.create(arr);
        return new ResponseEntity<Object>("자소서와 항목을 생성하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/test")
    @ApiOperation(value = "테스트용도")
    public ResponseEntity<Object> test(@RequestBody Map<String, Object> resume) {
        System.out.println(resume);
        ObjectMapper mapper = new ObjectMapper();
        List<Resumeitem> arr = mapper.convertValue(resume.get("resumeItems"), new TypeReference<List<Resumeitem>>() {
        });

        System.out.println(arr.size());
        for (Resumeitem r : arr) {
            System.out.println(r.getTitle());
            System.out.println(r.getContent());
        }
        return new ResponseEntity<Object>(arr, HttpStatus.OK);
    }

    @DeleteMapping("/resume")
    @ApiOperation(value = "")
    public ResponseEntity<Object> delete(int resumeId) {
        if (!resumeService.delete(resumeId)) {
            return new ResponseEntity<Object>("삭제에 실패했습니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Object>("삭제에 성공했습니다.", HttpStatus.OK);
    }

    @PutMapping("/resume")
    @ApiOperation(value = "")
    public ResponseEntity<Object> update(@RequestParam int resumeId, @RequestParam String title,
            @RequestParam String company, @RequestParam String job, @RequestParam String category) {
        Resume resume = resumeService.read(resumeId);
        resume.setCompany(company);
        resume.setJob(job);
        resume.setTitle(title);
        resume.setCategory(category);
        if (!resumeService.update(resume)) {
            return new ResponseEntity<Object>("업데이트에 실패하였습니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Object>("업데이트에 성공하였습니다.", HttpStatus.OK);
    }
}