package com.web.blog.dao.study;

import java.util.List;
import java.util.Set;

import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

public interface StudyApprovalDao {
	public boolean apply(Study study, User member);
	public boolean disapply(Study study, User member);
	public boolean isExistMemberApproval(Study study, User member);
	public Set<User> getMemberApproval(Study study);
	Set<User> getStudyApprovalUserList(Study study);
}
