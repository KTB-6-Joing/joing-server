package com.ktb.joing.domain.user.dto.response;

public record ProfileEvaluationResponse(
        boolean evaluationStatus,
        Long subscribers,
        String channelImage,
        String reason
) {
    public static ProfileEvaluationResponse from(boolean evaluationStatus, Long subscribers, String channelImage, String reason) {
        return new ProfileEvaluationResponse(
                evaluationStatus,
                subscribers,
                channelImage,
                reason
        );
    }
}
