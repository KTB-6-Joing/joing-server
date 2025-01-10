package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;

import java.util.List;

public record ItemResponse(
        Long id,
        String nickname,
        String title,
        String content,
        MediaType mediaType,
        Category category,
        List<EtcResponse> etcs
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getProductManager().getNickname(),
                item.getTitle(),
                item.getContent(),
                item.getMediaType(),
                item.getCategory(),
                item.getEtcs().stream()
                        .map(EtcResponse::from)
                        .toList()
        );
    }
}
