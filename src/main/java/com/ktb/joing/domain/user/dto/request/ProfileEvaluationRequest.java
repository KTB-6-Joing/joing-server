package com.ktb.joing.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileEvaluationRequest {
    @JsonProperty("channel_id")
    private String channelId;

    public ProfileEvaluationRequest(String channelId) {
        this.channelId = channelId;
    }
}
