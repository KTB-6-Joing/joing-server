package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.entity.MatchingStatus;

public record MatchingListResponse(
        Long matchingId,
        String title,
        MatchingStatus status
) {
    public static MatchingListResponse from(Matching matching) {
        return new MatchingListResponse(
                matching.getId(),
                matching.getItem().getTitle(),
                matching.getStatus()
        );
    }
}
