package com.ktb.joing.domain.item.dto.response;

import java.util.List;

public record FeedbackResponse(
        int feedbackType,
        float currentScore,
        String comment,
        List<String> violations
) {
    public FeedbackView toView() {
        return new FeedbackView(comment);
    }
}
