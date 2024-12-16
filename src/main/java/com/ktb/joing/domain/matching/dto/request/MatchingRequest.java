package com.ktb.joing.domain.matching.dto.request;

import com.ktb.joing.domain.matching.entity.MatchingSender;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingRequest {
    @NotNull
    private Long itemId;
    @NotNull
    private Long creatorId;
    @NotNull
    private MatchingSender sender;
}
