package com.nb.dev.mini_x.services;

import com.nb.dev.mini_x.dtos.request.TweetRequest;
import com.nb.dev.mini_x.dtos.response.FeedResponse;
import com.nb.dev.mini_x.dtos.response.FeedTweetResponse;
import com.nb.dev.mini_x.entities.Tweet;
import com.nb.dev.mini_x.enums.Values;
import com.nb.dev.mini_x.repositories.TweetRepository;
import com.nb.dev.mini_x.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
        Tweet tweet = new Tweet(user.get(), tweetRequest.post());
        tweetRepository.save(tweet);

    }

    public void deleteTweet(Long id, JwtAuthenticationToken token){
        Tweet tweet = tweetRepository
                .findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user = userRepository.findById(UUID.fromString(token.getName()));

        boolean isAdmin = user
                .get()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Values.ADMIN.name()));

        boolean isAuthor = tweet.getUser().getId().equals(UUID.fromString(token.getName()));

        if(isAuthor || isAdmin){
            tweetRepository.deleteById(id);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);


    }

    public FeedResponse feed(int pageNumber, int pageSize){

      List<FeedTweetResponse> tweets =  tweetRepository
                .findAll(PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.Direction.DESC, "postTime"))
                .stream()
                .map(tweet -> new FeedTweetResponse(
                        tweet.getId(),
                        tweet.getPost(),
                        tweet.getUser().getUserName()
                )).toList();

      return new FeedResponse(tweets, pageNumber, pageSize, 10, tweets.size());
    }
}
