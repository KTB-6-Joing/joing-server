package com.ktb.joing.domain.recommend.service;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.recommend.client.RecommendAIClient;
import com.ktb.joing.domain.recommend.dto.request.CreatorRecommendRequest;
import com.ktb.joing.domain.recommend.dto.request.ItemRecommendRequest;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommend;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendView;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommend;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendView;
import com.ktb.joing.domain.recommend.exception.RecommendErrorCode;
import com.ktb.joing.domain.recommend.exception.RecommendException;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecommendService {
    private final RecommendAIClient recommendAIClient;
    private final ItemRepository itemRepository;
    private final CreatorRepository creatorRepository;

    // 기획안를 가지고 -> 크리에이터 추천
    public Mono<List<CreatorRecommendView>> getRecommendedCreators(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        // 권한 체크
        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        // AI 추천 요청 생성
        CreatorRecommendRequest request = creatorRecommendRequest(item);

        return recommendAIClient.requestCreatorRecommend(request)
                .map(response -> response.getRecommendedCreators().stream()
                        .map(this::mapToCreatorRecommendView)
                        .collect(Collectors.toList()))
                .onErrorMap(e -> new RecommendException(RecommendErrorCode.AI_RECOMMEND_FAILED));
    }

    // 크리에이터 가지고 -> 기획안 추천
    public Mono<List<ItemRecommendView>> getRecommendedItems(String username) {
        Creator creator = creatorRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // AI 추천 요청 생성
        ItemRecommendRequest request = itemRecommendRequest(creator);

        return recommendAIClient.requestItemRecommend(request)
                .map(response -> response.getRecommendedItems().stream()
                        .map(this::mapToItemRecommendView)
                        .collect(Collectors.toList()))
                .onErrorMap(e -> new RecommendException(RecommendErrorCode.AI_RECOMMEND_FAILED));
    }

    private CreatorRecommendRequest creatorRecommendRequest(Item item) {
        return CreatorRecommendRequest.builder()
                .title(item.getTitle())
                .category(item.getCategory().toString())
                .mediaType(item.getMediaType().toString().toLowerCase())
                .score(item.getScore())
                .content(item.getContent())
                .build();
    }

    private ItemRecommendRequest itemRecommendRequest(Creator creator) {
        return ItemRecommendRequest.builder()
                .nickname(creator.getNickname())
                .category(creator.getCategory().toString())
                .subscribers(creator.getSubscribers())
                .build();
    }

    private CreatorRecommendView mapToCreatorRecommendView(CreatorRecommend recommend) {
        Creator creator = creatorRepository.findById(recommend.getCreatorId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        return CreatorRecommendView.builder()
                .profileImage(creator.getProfileImage())
                .nickname(creator.getNickname())
                .channelUrl(creator.getChannelUrl())
                .build();
    }

    private ItemRecommendView mapToItemRecommendView(ItemRecommend recommend) {
        Item item = itemRepository.findById(recommend.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        return ItemRecommendView.builder()
                .title(recommend.getTitle())
                .content(recommend.getContent())
                .keywords(item.getSummary().getKeyword())
                .build();
    }
}
