package com.web.blog.model.reply;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.blog.model.resume.Resume;
import com.web.blog.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
        // `id` int(11) NOT NULL AUTO_INCREMENT,
        // `user_id` int(11) NOT NULL,
        // `resume_id` int(11) NOT NULL,
        // `content` varchar(500) NOT NULL,
        // PRIMARY KEY (`id`),
        // FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
        // FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @JsonIgnore
        @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
                        CascadeType.MERGE, CascadeType.DETACH })
        @JoinColumn(name = "user_id")
        private User user;

        @JsonIgnore
        @ManyToOne(targetEntity = Resume.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
                        CascadeType.MERGE, CascadeType.DETACH })
        @JoinColumn(name = "resume_id")
        private Resume resume;

        @NotNull(message = "내용은 필수 항목입니다.")
        private String content;

}