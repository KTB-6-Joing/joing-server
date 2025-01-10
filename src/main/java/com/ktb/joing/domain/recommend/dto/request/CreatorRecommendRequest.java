package com.ktb.joing.domain.recommend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktb.joing.domain.item.entity.Item;

public record CreatorRecommendRequest(
        String title,
        @JsonProperty("item_category")
        String category,
        String mediaType,
        float score,
        @JsonProperty("item_content")
        String content
) {
    public static CreatorRecommendRequest from(Item item) {
        return new CreatorRecommendRequest(
                item.getTitle(),
                item.getCategory().toString(),
                item.getMediaType().toString().toLowerCase(),
                item.getScore(),
                item.getContent()
        );
    }
}
