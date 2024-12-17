package com.ktb.joing.domain.matching.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingRequestToCreator {
    @NotNull
    private Long itemId;
    @NotNull
    private Long creatorId;
}
