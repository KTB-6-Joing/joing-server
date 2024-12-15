package com.ktb.joing.domain.recommend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRecommendRequest {
    @JsonProperty("channel_name")
    private String nickname;
    @JsonProperty("channel_category")
    private String category;
    private Long subscribers;

    @Builder
    public ItemRecommendRequest(String nickname, String category, Long subscribers) {
        this.nickname = nickname;
        this.category = category;
        this.subscribers = subscribers;
    }
}
