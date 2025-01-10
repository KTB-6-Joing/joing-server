package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;

public record ItemRecentResponse(
    Long id,
    String title,
    SummaryView summaryView
) {
    public static ItemRecentResponse from(Item item) {
        return new ItemRecentResponse(
                item.getId(),
                item.getTitle(),
                item.getSummary() != null ?
                        SummaryView.from(item.getSummary())
                        : null
        );
    }
}
