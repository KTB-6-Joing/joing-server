package com.ktb.joing.domain.matching.dto.request;

import com.ktb.joing.domain.matching.entity.MatchingStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingStatusRequest {
    private MatchingStatus status;

    @Builder
    public MatchingStatusRequest(MatchingStatus status) {
        this.status = status;
    }
}
