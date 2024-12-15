package com.ktb.joing.domain.recommend.service;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.recommend.client.RecommendAIClient;
import com.ktb.joing.domain.recommend.dto.request.CreatorRecommendRequest;
import com.ktb.joing.domain.recommend.dto.request.ItemRecommendRequest;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendResponse;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendResponse;
import com.ktb.joing.domain.recommend.exception.RecommendErrorCode;
import com.ktb.joing.domain.recommend.exception.RecommendException;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecommendService {
    private final RecommendAIClient recommendAIClient;
    private final ItemRepository itemRepository;
    private final CreatorRepository creatorRepository;

    // 기획안를 가지고 -> 크리에이터 추천
    public Mono<CreatorRecommendResponse> getRecommendedCreators(Long itemId, String username){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        // 권한 체크
        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        // AI 추천 요청 생성
        CreatorRecommendRequest request = creatorRecommendRequest(item);

        return recommendAIClient.requestCreatorRecommend(request)
                .onErrorMap(e -> new RecommendException(RecommendErrorCode.AI_RECOMMEND_FAILED));

    }

    // 크리에이터 가지고 -> 기획안 추천
    public Mono<ItemRecommendResponse> getRecommendedItems(Long userId, String username){
        Creator creator = creatorRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // AI 추천 요청 생성
        ItemRecommendRequest request = itemRecommendRequest(creator);

        // 권한 체크
        if(!creator.getUsername().equals(username)){
            throw new UserException(UserErrorCode.USER_NOT_AUTHORIZED);
        }

        return recommendAIClient.requestItemRecommend(request)
                .onErrorMap(e -> new RecommendException(RecommendErrorCode.AI_RECOMMEND_FAILED));
    }

    private CreatorRecommendRequest creatorRecommendRequest(Item item) {
        return CreatorRecommendRequest.builder()
                .title(item.getTitle())
                .category(item.getCategory().toString().toLowerCase())
                .mediaType(item.getMediaType().toString().toLowerCase())
                .score(item.getScore())
                .content(item.getContent())
                .build();
    }

    private ItemRecommendRequest itemRecommendRequest(Creator creator) {
        return ItemRecommendRequest.builder()
                .nickname(creator.getNickname())
                .category(creator.getCategory().toString().toLowerCase())
                .subscribers(creator.getSubscribers())
                .build();
    }

}
