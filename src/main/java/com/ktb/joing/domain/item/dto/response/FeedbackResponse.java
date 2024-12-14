package com.ktb.joing.domain.item.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackResponse {
    private int feedbackType;
    private float currentScore;
    private String comment;
    private List<String> violations;

    public FeedbackView toView() {
        return new FeedbackView(this.comment);
    }
}
