package com.ktb.joing.domain.matching.dto.request;

import jakarta.validation.constraints.NotNull;

public record MatchingRequestToItem(
        @NotNull
        Long itemId
) {}
