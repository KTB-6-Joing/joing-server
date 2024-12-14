package com.ktb.joing.domain.recommend.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRecommendResponse {
    private List<ItemRecommend> recommendedItems;
}
