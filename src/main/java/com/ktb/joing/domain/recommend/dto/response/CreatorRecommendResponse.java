package com.ktb.joing.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorRecommendResponse {
    @JsonProperty("recommend_creators")
    private List<CreatorRecommend> recommendCreators;
}
