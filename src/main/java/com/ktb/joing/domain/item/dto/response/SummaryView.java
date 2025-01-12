package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Summary;

import java.util.Arrays;
import java.util.List;

public record SummaryView(
        String title,
        String content,
        List<String> keywords
) {
    public static SummaryView from(Summary summary) {
        return new SummaryView(
                summary.getTitle(),
                summary.getContent(),
                Arrays.asList(summary.getKeyword().split(","))
        );
    }
}
