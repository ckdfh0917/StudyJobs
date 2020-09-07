package com.web.blog.service.study;

import java.util.List;
import java.util.Map;

import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

public interface StudyMemberService {
	public boolean approve(Study study, User user);
	public boolean disapprove(Study study, User user);
	
	public boolean leave(Study study, User user);
	public boolean isFull(final Study study);
	public boolean isExistMember(final Study study, final User user);
	public List<Map<String, String>> getStudyApprovalUserList(Study study);
	public List<Map<String, String>> getStudyUserList(Study study);
	List<Map<String, String>> getUserApplyingStudyList(User user);
}
