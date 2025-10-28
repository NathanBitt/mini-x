package com.nb.dev.mini_x.entities;

import com.nb.dev.mini_x.enums.Status;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.time.Instant;

@Entity
@Table(name = "tbl_tweets")
@SQLDelete(sql = "UPDATE tbl_tweets SET is_deleted=1 WHERE id=?")
@Where(clause = "is_deleted=0")
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
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_deleted")
    private Status isDeleted = Status.ACTIVE;

    private Tweet(){}

    public Tweet(User user, String post) {
        this.user = user;
        this.post = post;
    }

    public Tweet(User user, String post, Instant postTime, Status isDeleted) {
        this.user = user;
        this.post = post;
        this.postTime = postTime;
        this.isDeleted = isDeleted;
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

    public Long getId() {
        return id;
    }

    public Status getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Status isDeleted) {
        this.isDeleted = isDeleted;
    }
}
