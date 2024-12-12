package com.ktb.joing.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorRecommend {
    @JsonProperty("creator_id")
    private Long creatorId;
    @JsonProperty("channel_category")
    private String category;
    @JsonProperty("channel_name")
    private String nickname;
    private Long subscribers;
}
