package com.ktb.joing.domain.recommend.client;

import com.ktb.joing.common.util.webClient.ReactiveHttpService;
import com.ktb.joing.domain.recommend.dto.request.CreatorRecommendRequest;
import com.ktb.joing.domain.recommend.dto.request.ItemRecommendRequest;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendResponse;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendAIClient {
    @Value("${ai.url.rec}")
    private String aiUrl;

    private final ReactiveHttpService reactiveHttpService;

    // 기획안으로 -> 크리에이터 추천 받음
    public Mono<CreatorRecommendResponse> requestCreatorRecommend(CreatorRecommendRequest request) {
        return reactiveHttpService.post(
                aiUrl + "/rec/recommend/creator",
                request,
                CreatorRecommendResponse.class
        ).doOnError(e -> log.error("AI 크리에이터 추천 요청 실패: {}", e.getMessage()));
    }

    // 크리에이터로 -> 기획안 추천 받음
    public Mono<ItemRecommendResponse> requestItemRecommend(ItemRecommendRequest request){
        return reactiveHttpService.post(
                aiUrl + "/rec/recommend/item",
                request,
                ItemRecommendResponse.class
        ).doOnError(e -> log.error("AI 기획안 추천 요청 실패: {}", e.getMessage()));
    }
}
