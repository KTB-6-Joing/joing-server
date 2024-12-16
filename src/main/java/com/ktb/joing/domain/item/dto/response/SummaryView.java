package com.ktb.joing.domain.item.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryView {
    private String title;
    private String content;
    private List<String> keywords;

    @Builder
    public SummaryView(String title, String content, List<String> keywords) {
        this.title = title;
        this.content = content;
        this.keywords = keywords;
    }
}
