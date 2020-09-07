package com.web.blog.model.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.web.blog.model.address.Address;
import com.web.blog.model.board.Post;
import com.web.blog.model.user.User;
import com.web.blog.model.study.Week;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Study {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_id")
	private int studyId;

	@JsonIgnore
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "mgr_id")
	private User user;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JoinTable(name = "study_member", joinColumns = @JoinColumn(name = "study_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> members;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JoinTable(name = "study_approval", joinColumns = @JoinColumn(name = "study_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> memberApproval;
	
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "study")
	// @JoinTable(name = "post", joinColumns = @JoinColumn(name = "study_id"))
	private Set<Post> posts;

	@JsonIgnore
	@ManyToOne(targetEntity = Address.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

	@JsonIgnore
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Period.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "period_id")
	private Period period;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Place.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Shift.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "shift_id")
	private Shift shift;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Week.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "week_id")
	private Week week;
	
	@Version
	private Long version;

	private String title;
	private String content;
	private LocalDate startDate;
	private LocalDate endDate;
	private int maxMembers;
	private int numMeetings;

	public List<Post> getPostList(){
		return new ArrayList<>(posts);
	}

	public boolean addPost(Post post) {
		if (this.posts == null) {
			this.posts = new HashSet<>();
		}
		if (!this.posts.contains(post)) {
			this.posts.add(post);
			return true;
		}
		return false;
	}
}
