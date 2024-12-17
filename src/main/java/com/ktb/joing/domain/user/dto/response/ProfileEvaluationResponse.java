package com.ktb.joing.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationResponse {
    private boolean evaluationStatus;
    private String channelImage;
    private String reason;

    @Builder
    public ProfileEvaluationResponse(boolean evaluationStatus, String channelImage, String reason) {
        this.evaluationStatus = evaluationStatus;
        this.channelImage = channelImage;
        this.reason = reason;
    }
}
