package com.web.blog.service.study;

import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

public interface StudyApprovalService {

	boolean disapply(Study study, User user);
	public boolean apply(Study study, User user);
	boolean in(Study study, User user);

	

}
