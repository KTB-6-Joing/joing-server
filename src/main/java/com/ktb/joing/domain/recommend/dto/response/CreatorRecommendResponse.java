package com.ktb.joing.domain.recommend.dto.response;

import java.util.List;

public record CreatorRecommendResponse(
        List<CreatorRecommend> recommendedCreators
) {}
