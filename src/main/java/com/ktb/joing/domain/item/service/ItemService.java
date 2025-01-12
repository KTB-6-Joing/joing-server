package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.request.ItemUpdateRequest;
import com.ktb.joing.domain.item.dto.response.ItemRecentResponse;
import com.ktb.joing.domain.item.dto.response.ItemDetailResponse;
import com.ktb.joing.domain.item.dto.response.ItemResponse;
import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.user.entity.ProductManager;
import com.ktb.joing.domain.user.repository.ProductManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ProductManagerRepository productManagerRepository;

    // 기획안 생성
    @Transactional
    public ItemResponse createItem(ItemCreateRequest request, String username) {
        ProductManager productManager = productManagerRepository.findByUsername(username)
                .orElseThrow(() -> new ItemException(ItemErrorCode.INVALID_USER_TYPE));

        Item item = Item.builder()
                .title(request.title())
                .content(request.content())
                .mediaType(request.mediaType())
                .category(request.category())
                .build();

        item.setProductManager(productManager);

        if (request.etcs() != null && !request.etcs().isEmpty()) {
            request.etcs().forEach(etcRequest -> {
                Etc etc = Etc.builder()
                        .name(etcRequest.name())
                        .value(etcRequest.value())
                        .build();
                item.addEtc(etc);
            });
        }

        itemRepository.save(item);
        return ItemResponse.from(item);
    }

    // 기획안 단 건 조회
    public ItemDetailResponse getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        return ItemDetailResponse.from(item);
    }

    // 기획안 리스트 조회 - 특정 기획자 사용자의 최근 3개월 기획안 기록 조회
    public List<ItemRecentResponse> getRecentItems(String username) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        return itemRepository.findRecentItems(username, threeMonthsAgo)
                .stream()
                .map(ItemRecentResponse::from)
                .toList();
    }

    // 기획안 수정
    @Transactional
    public ItemDetailResponse updateItem(Long itemId, ItemUpdateRequest request, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        item.update(request.title(),
                request.content(),
                request.mediaType(),
                request.category());

        if (request.etcs() != null) {
            List<Etc> newEtcs = request.etcs().stream()
                    .map(etcRequest -> Etc.builder()
                            .name(etcRequest.name())
                            .value(etcRequest.value())
                            .build())
                    .collect(Collectors.toList());

            item.updateEtcs(newEtcs);
        }

        return ItemDetailResponse.from(item);
    }

    // 기획안 삭제
    @Transactional
    public void deleteItem(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getProductManager().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }
        itemRepository.delete(item);
    }

}
