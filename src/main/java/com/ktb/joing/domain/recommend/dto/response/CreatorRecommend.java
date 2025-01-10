package com.ktb.joing.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatorRecommend(
        @JsonProperty("creator_id")
        Long creatorId,
        @JsonProperty("channel_category")
        String category,
        @JsonProperty("channel_name")
        String nickname,
        Long subscribers
) {}