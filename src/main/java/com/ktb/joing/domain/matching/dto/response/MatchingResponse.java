package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import com.ktb.joing.domain.matching.entity.Matching;

public record MatchingResponse(
        Long matchingId,
        Long itemId,
        Long creatorId,
        MatchingStatus status,
        MatchingSender sender
) {
    public static MatchingResponse from(Matching matching) {
        return new MatchingResponse(
                matching.getId(),
                matching.getItem().getId(),
                matching.getCreator().getId(),
                matching.getStatus(),
                matching.getSender()
        );
    }
}
