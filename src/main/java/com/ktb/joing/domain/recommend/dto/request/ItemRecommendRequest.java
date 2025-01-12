package com.ktb.joing.domain.recommend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktb.joing.domain.user.entity.Creator;

public record ItemRecommendRequest(
        @JsonProperty("channel_name")
        String nickname,
        @JsonProperty("channel_category")
        String category,
        Long subscribers
) {
    public static ItemRecommendRequest from(Creator creator){
        return new ItemRecommendRequest(
                creator.getNickname(),
                creator.getCategory().toString(),
                creator.getSubscribers()
        );
    }
}
