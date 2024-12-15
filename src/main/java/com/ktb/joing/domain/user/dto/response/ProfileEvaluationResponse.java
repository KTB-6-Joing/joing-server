package com.ktb.joing.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationResponse {
    private boolean evaluation_status;
    private String reason;

    @Builder
    public ProfileEvaluationResponse(boolean evaluation_status, String reason) {
        this.evaluation_status = evaluation_status;
        this.reason = reason;
    }
}
