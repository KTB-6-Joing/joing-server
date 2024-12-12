package com.ktb.joing.domain.recommend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorRecommendRequest {
    private String title;
    @JsonProperty("item_Category")
    private String category;
    @JsonProperty("media_type")
    private String mediaType;
    private float score;
    @JsonProperty("item_content")
    private String content;

    @Builder
    public CreatorRecommendRequest(String title, String category, String mediaType, float score, String content){
        this.title = title;
        this.category = category;
        this.mediaType = mediaType;
        this.score = score;
        this.content = content;
    }
}
