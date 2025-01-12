package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.client.ItemAIClient;
import com.ktb.joing.domain.item.dto.request.ItemEvaluationRequest;
import com.ktb.joing.domain.item.dto.response.EvaluationResponse;
import com.ktb.joing.domain.item.dto.response.ItemEvaluationResponse;
import com.ktb.joing.domain.item.dto.response.ResponseType;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemEvaluationService {

    private final ItemRepository itemRepository;
    private final ItemAIClient itemAIClient;
    private final ItemSummaryService itemSummaryService;

    public Mono<EvaluationResponse<?>> requestEvaluation(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        ItemEvaluationRequest request = createEvaluationRequest(item);

        return itemAIClient.requestEvaluation(request)
                .doOnSuccess(response -> {
                    if (response.evaluationResult() == 1) {
                        itemSummaryService.updateItemSummary(item, response.summary());
                    }
                })
                .<EvaluationResponse<?>>map(this::convertToEvaluationResponse)
                .onErrorMap(e -> new ItemException(ItemErrorCode.AI_EVALUATION_FAILED));
    }

    private EvaluationResponse<?> convertToEvaluationResponse(ItemEvaluationResponse response) {
        if (response.evaluationResult() == 0) {
            return new EvaluationResponse<>(ResponseType.FEEDBACK,
                    response.feedback().toView());
        } else {
            return new EvaluationResponse<>(ResponseType.SUMMARY,
                    response.summary().toView());
        }
    }

    private ItemEvaluationRequest createEvaluationRequest(Item item) {
        return ItemEvaluationRequest.from(item);
    }
}
