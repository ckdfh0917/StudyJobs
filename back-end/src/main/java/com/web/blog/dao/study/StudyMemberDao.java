package com.web.blog.dao.study;

import java.util.Set;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

public interface StudyMemberDao {
	public boolean addMember(Study study, User member);
	public boolean removeMember(Study study, User member);
	public boolean isFull(Study study);
	public boolean isExistMember(Study study, User member);
	boolean approve(Study study, User member);
	boolean disapprove(Study study, User member);
	Set<User> getStudyUserList(Study study);
}
