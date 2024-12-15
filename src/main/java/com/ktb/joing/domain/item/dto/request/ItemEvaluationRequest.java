package com.ktb.joing.domain.item.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ItemEvaluationRequest {
    private String title;
    private String content;
    private String mediaType;
    private float proposalScore;
    private Map<String, String> additionalFeatures;

    @Builder
    public ItemEvaluationRequest(String title, String content, String mediaType,
                                 float proposalScore, Map<String, String> additionalFeatures) {
        this.title = title;
        this.content = content;
        this.mediaType = mediaType;
        this.proposalScore = proposalScore;
        this.additionalFeatures = additionalFeatures;
    }
}
