package com.web.blog.model.resume;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.blog.model.reply.Reply;
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
public class Resume {

    // `id` int(11) NOT NULL AUTO_INCREMENT,
    // `writer` int(11) NOT NULL,
    // `company` varchar(50) NOT NULL,
    // `job` varchar(500) NOT NULL,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "분류는 필수 항목입니다.")
    private String category;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH })
    @JoinColumn(name = "writer")
    private User user;

    @NotNull(message = "제목은 필수 항목입니다.")
    private String title;

    @NotNull(message = "지원 회사는 필수 항목입니다.")
    private String company;

    @NotNull(message = "지원 직무는 필수 항목입니다.")
    private String job;

    @JsonIgnore
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "resume")
    private Set<Resumeitem> resumeItems;

    @JsonIgnore
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "resume")
    private Set<Reply> reply;
}