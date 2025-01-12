package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;

import java.util.Arrays;
import java.util.List;

public record MatchingDetailResponse(
        String productManagerNickname,
        String productManagerEmail,
        Long itemId,
        String itemTitle,
        String itemContent,
        List<String> itemKeyword,
        String creatorNickname,
        String creatorProfileImage,
        String creatorEmail,
        String creatorChannelUrl,
        MatchingStatus status,
        MatchingSender sender
) {
    public static MatchingDetailResponse from(Matching matching) {
        return new MatchingDetailResponse(
                matching.getItem().getProductManager().getNickname(),
                matching.getItem().getProductManager().getEmail(),
                matching.getItem().getId(),
                matching.getItem().getTitle(),
                matching.getItem().getSummary().getContent(),
                Arrays.asList(matching.getItem().getSummary().getKeyword().split(",")),
                matching.getCreator().getNickname(),
                matching.getCreator().getProfileImage(),
                matching.getCreator().getEmail(),
                matching.getCreator().getChannelUrl(),
                matching.getStatus(),
                matching.getSender()
        );
    }
}
