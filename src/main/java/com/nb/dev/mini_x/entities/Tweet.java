package com.nb.dev.mini_x.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tbl_tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String post;
    @CreationTimestamp
    private Instant postTime;

    public Tweet(User user, String post, Instant postTime) {
        this.user = user;
        this.post = post;
        this.postTime = postTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Instant getPostTime() {
        return postTime;
    }

    public void setPostTime(Instant postTime) {
        this.postTime = postTime;
    }
}
