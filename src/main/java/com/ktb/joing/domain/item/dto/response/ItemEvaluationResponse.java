package com.ktb.joing.domain.item.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEvaluationResponse {
    private int evaluationResult;
    private FeedbackResponse feedback;
    private SummaryResponse summary;
}
