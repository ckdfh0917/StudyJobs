package com.web.blog.dao.study;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

@Repository
public class StudyApprovalDaoImpl implements StudyApprovalDao {

	@Override
	public boolean apply(Study study, User member) {
		Set<User> membersApproval = study.getMemberApproval();
		if(membersApproval==null) {
			membersApproval = new HashSet<>();
			boolean ret = membersApproval.add(member);
			study.setMembers(membersApproval);
			return ret;
		}
		else return membersApproval.add(member);
	}

	@Override
	public boolean disapply(Study study, User member) {
		Set<User> members = study.getMemberApproval();
		if(members!=null && !members.isEmpty() && members.contains(member))
			return members.remove(member);
		return false;
	}
	
	@Override
	public Set<User> getMemberApproval(Study study) {
		return study.getMemberApproval();
	}
	
	
	@Override
	public boolean isExistMemberApproval(Study study, User member) {
		Set<User> members = study.getMemberApproval();
		return (members==null || members.isEmpty()) ? false : members.contains(member);
	}

	@Override
	public Set<User> getStudyApprovalUserList(Study study) {
		return study.getMemberApproval();
	}

	

}
