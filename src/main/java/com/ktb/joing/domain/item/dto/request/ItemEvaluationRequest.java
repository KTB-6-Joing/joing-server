package com.ktb.joing.domain.item.dto.request;

import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;

import java.util.Map;
import java.util.stream.Collectors;

public record ItemEvaluationRequest(
        String title,
        String content,
        String mediaType,
        float proposalScore,
        Map<String, String> additionalFeatures
) {
    public static ItemEvaluationRequest from(Item item) {
        return new ItemEvaluationRequest(
                item.getTitle(),
                item.getContent(),
                item.getMediaType().toString().toLowerCase(),
                item.getScore(),
                item.getEtcs().stream()
                        .collect(Collectors.toMap(
                                Etc::getName,
                                Etc::getValue
                        ))
        );
    }
}
