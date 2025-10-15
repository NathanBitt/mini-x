package com.nb.dev.mini_x.dtos.response;

import java.util.List;

public record FeedResponse(List<FeedTweetResponse> feedTweets,
                           int page,
                           int pageSize,
                           int totalPages,
                           int totalElements){
}
