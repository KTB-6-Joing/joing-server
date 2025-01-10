package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;

import java.util.List;

public record ItemDetailResponse(
        Long id,
        String nickname,
        String email,
        String profileImage,
        String title,
        String content,
        MediaType mediaType,
        Category category,
        List<EtcResponse> etcs,
        SummaryView summaryView
) {
    public static ItemDetailResponse from(Item item) {
        return new ItemDetailResponse(item.getId(),
                item.getProductManager().getNickname(),
                item.getProductManager().getEmail(),
                item.getProductManager().getProfileImage(),
                item.getTitle(),
                item.getContent(),
                item.getMediaType(),
                item.getCategory(),
                item.getEtcs().stream()
                        .map(EtcResponse::from)
                        .toList(),
                item.getSummary() != null
                        ? SummaryView.from(item.getSummary())
                        : null
        );
    }
}
