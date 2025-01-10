package com.ktb.joing.domain.item.dto.response;

public record ItemEvaluationResponse(
        int evaluationResult,
        FeedbackResponse feedback,
        SummaryResponse summary
) {}
