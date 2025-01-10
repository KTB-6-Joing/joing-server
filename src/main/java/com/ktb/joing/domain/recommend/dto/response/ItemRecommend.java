package com.ktb.joing.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemRecommend(
        Long itemId,
        String title,
        @JsonProperty("item_category")
        String category,
        String mediaType,
        float score,
        @JsonProperty("item_content")
        String content
) {}
