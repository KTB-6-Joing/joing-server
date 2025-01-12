package com.ktb.joing.domain.item.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SummaryResponse(
        String title,
        String content,
        @JsonProperty("keyword")
        List<String> keywords
) {
    public SummaryView toView() {
        return new SummaryView(title, content, keywords);
    }
}
