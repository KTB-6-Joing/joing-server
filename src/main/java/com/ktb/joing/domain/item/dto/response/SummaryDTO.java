package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Summary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryDTO {
    private String title;
    private String content;
    private List<String> keywords;

    @Builder
    public SummaryDTO(Summary summary) {
        this.title = summary.getTitle();
        this.content = summary.getContent();
        this.keywords = Arrays.asList(summary.getKeyword().split(","));
    }
}
