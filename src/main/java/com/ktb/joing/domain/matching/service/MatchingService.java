package com.ktb.joing.domain.matching.service;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import com.ktb.joing.domain.matching.dto.request.MatchingRequest;
import com.ktb.joing.domain.matching.dto.response.MatchingResponse;
import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.exception.MatchingErrorCode;
import com.ktb.joing.domain.matching.exception.MatchingException;
import com.ktb.joing.domain.matching.repository.MatchingRepository;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final ItemRepository itemRepository;
    private final CreatorRepository creatorRepository;

    // 매칭 요청 생성
    public MatchingResponse createMatching(MatchingRequest request, String username) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
        Creator creator = creatorRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        //권한 검증 - 요청을 보내는 사람이 실제로 기획안 작성인/크리에이터 본인이 맞는지 확인
        if (request.getSender() == MatchingSender.PRODUCT_MANAGER) {
            if (!item.getProductManager().getUsername().equals(username)) {
                throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
            }
        }else{
            if (!creator.getUsername().equals(username)) {
                throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
            }
        }

        Matching matching = Matching.builder()
                .item(item)
                .creator(creator)
                .sender(request.getSender())
                .build();

        matching = matchingRepository.save(matching);
        return new MatchingResponse(matching);
    }

    // 매칭 상태 조회
    public MatchingResponse getMatchingStatus(Long matchingId, String username) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));

        // 권한 검증 - 매칭 참여자만 조회 가능
        if (!matching.getCreator().getUsername().equals(username) &&
                !matching.getItem().getProductManager().getUsername().equals(username)) {
            throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
        }

        return new MatchingResponse(matching);
    }

    // 매칭 취소
    public void cancelMatching(Long matchingId, String username) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));
        log.info("매칭 id로 매칭 찾기 {}", matching.toString());

        // 권한 검증 - 매칭을 요청한 사람만 취소 가능
        if (matching.getSender() == MatchingSender.PRODUCT_MANAGER) {
            if (!matching.getItem().getProductManager().getUsername().equals(username)) {
                log.info("요청한 사람이 기획자");
                throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
            }
        } else {
            if (!matching.getCreator().getUsername().equals(username)) {
                throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
            }
        }

        if (matching.getStatus() != MatchingStatus.PENDING) {
            throw new MatchingException(MatchingErrorCode.INVALID_MATCHING_STATUS);
        }

        matching.cancel();
        matchingRepository.save(matching);
    }

    // 매칭 답장에 대한 응답 - 매칭 상태 변경 (수락/ 거절)
    public MatchingResponse updateMatchingStatus(Long matchingId, MatchingStatus newStatus, String username) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));

        // 권한 검증 - 매칭 요청을 받은 사람만 응답 가능
        if (!matching.isReceiver(username)) {
            throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
        }

        if (matching.getStatus() != MatchingStatus.PENDING) {
            throw new MatchingException(MatchingErrorCode.MATCHING_CANCEL_NOT_ALLOWED);
        }

        if (newStatus != MatchingStatus.ACCEPTED && newStatus != MatchingStatus.REJECTED) {
            throw new MatchingException(MatchingErrorCode.INVALID_MATCHING_STATUS);
        }

        matching.updateStatus(newStatus);
        matching = matchingRepository.save(matching);
        return new MatchingResponse(matching);
    }

}
