package com.ktb.joing.domain.recommend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRecommendView {
    private Long id;
    private String title;
    private String content;
    private List<String> keywords;

    @Builder
    public ItemRecommendView(Long id, String title, String content, String keywords) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keywords = Arrays.asList(keywords.split(","));
    }
}
