package com.nb.dev.mini_x.repositories;

import com.nb.dev.mini_x.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
