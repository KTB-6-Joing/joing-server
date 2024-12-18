package com.ktb.joing.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRecommend {
    private Long itemId;
    private String title;
    @JsonProperty("item_category")
    private String category;
    private String mediaType;
    private float score;
    @JsonProperty("item_content")
    private String content;
}
