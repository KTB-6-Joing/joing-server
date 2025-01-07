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
    private boolean isMatched;

    // JPQL용 생성자 추가
    public ItemRecommendView(Long id, String title, String content, String keywords) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keywords = Arrays.asList(keywords.split(","));
    }

    @Builder
    public ItemRecommendView(Long id, String title, String content, List<String> keywords, boolean isMatched) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keywords = keywords;
        this.isMatched = isMatched;
    }
}
