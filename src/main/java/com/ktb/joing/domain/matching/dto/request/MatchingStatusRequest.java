package com.ktb.joing.domain.matching.dto.request;

import com.ktb.joing.domain.matching.entity.MatchingStatus;

public record MatchingStatusRequest(
        MatchingStatus status
) {}
