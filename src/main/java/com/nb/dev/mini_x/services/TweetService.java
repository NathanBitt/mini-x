package com.nb.dev.mini_x.services;

import com.nb.dev.mini_x.dtos.request.TweetRequest;
import com.nb.dev.mini_x.entities.Tweet;
import com.nb.dev.mini_x.repositories.TweetRepository;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TweetService {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public void newTweet(TweetRequest tweetRequest, JwtAuthenticationToken token){

        var user =  userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet(user.get(), tweetRequest.post());
        tweetRepository.save(tweet);

    }

    public void deleteTweet(Long id, JwtAuthenticationToken token){
        var tweet = tweetRepository
                .findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAuthor = tweet.getUser().getId().equals(UUID.fromString(token.getName()));

        if(isAuthor){
            tweetRepository.deleteById(id);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);


    }
}
