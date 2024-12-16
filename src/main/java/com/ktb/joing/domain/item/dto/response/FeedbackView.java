package com.ktb.joing.domain.item.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackView {
    private String comment;

    @Builder
    public FeedbackView(String comment) {
        this.comment = comment;
    }
}
