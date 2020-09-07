package com.web.blog.service.study;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.blog.dao.study.StudyDao;
import com.web.blog.dao.study.StudyMemberDao;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

@Service
@Transactional
public class StudyMemberServiceImpl implements StudyMemberService {
	@Autowired
	private StudyDao studyDao;
	
	
	
	@Override
	public boolean approve(Study study, User user) {
		return studyDao.approve(study, user);
	}
	
	@Override
	public boolean disapprove(Study study, User user) {

		return studyDao.disapprove(study, user);
	}
	
	@Override
	public boolean leave(Study study, User user) {
		return studyDao.removeMember(study, user);
	}

	@Override
	public boolean isFull(Study study) {
		return studyDao.isFull(study);
	}

	@Override
	public boolean isExistMember(Study study, User user) {
		return studyDao.isExistMember(study, user);
	}

	@Override
	public List <Map<String, String>> getStudyApprovalUserList(Study study) {
		
		return studyDao.getStudyApprovalUserList(study).stream().map(user->{
            Map<String,String> map=new HashMap<>();
            map.put("name",user.getName());
            map.put("email",user.getEmail());
            return map;
        }).collect(Collectors.toList());
	}

	@Override
	public List <Map<String, String>> getStudyUserList(Study study) {
		return studyDao.getStudyUserList(study).stream().map(user->{
            Map<String,String> map=new HashMap<>();
            map.put("name",user.getName());
            map.put("email",user.getEmail());
            return map;
        }).collect(Collectors.toList());
	}
	
	@Override
	public List <Map<String, String>> getUserApplyingStudyList(User user) {
		return user.getApprovingStudies().stream().map(study->{
            Map<String,String> map=new HashMap<>();
            map.put("id", Integer.toString(study.getStudyId()));
            map.put("title",study.getTitle());
            map.put("content",study.getContent());
            map.put("category",study.getCategory().getName());
            return map;
        }).collect(Collectors.toList());
	}
	
	
}
