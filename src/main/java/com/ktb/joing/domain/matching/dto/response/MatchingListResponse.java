package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.MatchingStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingListResponse {
    private Long matchingId;
    private String title;
    private MatchingStatus status;

    @Builder
    public MatchingListResponse(Long matchingId, String title, MatchingStatus status) {
        this.matchingId = matchingId;
        this.title = title;
        this.status = status;
    }
}
