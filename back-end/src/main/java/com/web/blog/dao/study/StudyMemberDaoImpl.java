package com.web.blog.dao.study;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

@Repository
public class StudyMemberDaoImpl implements StudyMemberDao {
	
	@Override
	public boolean approve(Study study, User member) {
		Set<User> members = study.getMembers();
		if(!study.getMemberApproval().remove(member)) return false;
		if(members==null) {
			members = new HashSet<>();
			boolean ret = members.add(member);
			study.setMembers(members);
			return ret;
		}
		else return members.add(member);
	}
	
	@Override
	public boolean disapprove(Study study, User member) {
		return study.getMemberApproval().remove(member);
	}

	@Override
	public boolean addMember(Study study, User member) {
		Set<User> members = study.getMembers();
		if(members==null) {
			members = new HashSet<>();
			boolean ret = members.add(member);
			study.setMembers(members);
			return ret;
		}
		else return members.add(member);
	}

	@Override
	public boolean removeMember(Study study, User member) {
		Set<User> members = study.getMembers();
		if(members!=null && !members.isEmpty() && members.contains(member))
			return members.remove(member);
		return false;
	}

	@Override
	public boolean isFull(Study study) {
		return study.getMembers().size() >= study.getMaxMembers();
	}
	
	@Override
	public boolean isExistMember(Study study, User member) {
		Set<User> members = study.getMembers();
		return (members==null || members.isEmpty()) ? false : members.contains(member);
	}

	@Override
	public Set<User> getStudyUserList(Study study) {
		
		return study.getMembers();
	}
}
