package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import com.ktb.joing.domain.matching.entity.Matching;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingResponse {
    private Long matchingId;
    private Long itemId;
    private Long creatorId;
    private MatchingStatus status;
    private MatchingSender sender;

    @Builder
    public MatchingResponse(Matching matching) {
        this.matchingId = matching.getId();
        this.itemId = matching.getItem().getId();
        this.creatorId = matching.getCreator().getId();
        this.status = matching.getStatus();
        this.sender = matching.getSender();
    }
}
