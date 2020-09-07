package com.web.blog.model.board;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.blog.model.study.Study;
import com.web.blog.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	/*
	 * id study_id writer title content
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JsonIgnore
	@ManyToOne(targetEntity = Study.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	private Study study;

	@JsonIgnore
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH })
	@JoinColumn(name = "writer")
	private User user;

	@NotNull(message = "제목은 필수 항목입니다.")
	private String title;

	@NotNull(message = "내용은 필수 항목입니다.")
	private String content;

	private LocalDate date;

	// @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
	// CascadeType.DETACH })
	// @JoinTable(name = "study_member", joinColumns = @JoinColumn(name =
	// "user_id"), inverseJoinColumns = @JoinColumn(name = "study_id"))
	// private Set<Study> studies;

	// public boolean addStudy(Study study) {
	// if (this.studies == null)
	// this.studies = new HashSet<>();

	// if (!this.studies.contains(study)) {
	// this.studies.add(study);
	// return true;
	// }
	// return false;
	// }

	// public boolean removeStudy(Study study) {
	// if (this.studies.contains(study)) {
	// this.studies.remove(study);
	// return true;
	// }
	// return false;
	// }
}
