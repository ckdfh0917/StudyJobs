package com.web.blog.service.study;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.blog.dao.study.StudyApprovalDao;
import com.web.blog.dao.study.StudyDao;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

@Transactional
@Service
public class StudyApprovalServiceImpl implements StudyApprovalService{
	
	
	@Autowired
	private StudyDao studyDao;
	@Override
	public boolean disapply(Study study, User user) {
		return studyDao.disapply(study,user); 
	}

	@Override
	public boolean apply(Study study, User user) {
		// TODO Auto-generated method stub
		//if(studyDao.isFull(study)) return false;		
		return studyDao.apply(study,user);
	}

	@Override
	public boolean in(Study study, User user) {
		
		return studyDao.isExistMemberApproval(study,user);
	}

}
