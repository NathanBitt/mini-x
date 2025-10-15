package com.nb.dev.mini_x.controllers;

import com.nb.dev.mini_x.dtos.request.TweetRequest;
import com.nb.dev.mini_x.services.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> newTweet(@RequestBody TweetRequest tweetRequest, JwtAuthenticationToken token){
        tweetService.newTweet(tweetRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, JwtAuthenticationToken token){
        tweetService.deleteTweet(id, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
