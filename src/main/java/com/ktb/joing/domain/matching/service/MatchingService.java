package com.ktb.joing.domain.matching.service;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.matching.dto.request.MatchingRequestToCreator;
import com.ktb.joing.domain.matching.dto.request.MatchingRequestToItem;
import com.ktb.joing.domain.matching.dto.response.MatchingDetailResponse;
import com.ktb.joing.domain.matching.dto.response.MatchingListResponse;
import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import com.ktb.joing.domain.matching.dto.response.MatchingResponse;
import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.exception.MatchingErrorCode;
import com.ktb.joing.domain.matching.exception.MatchingException;
import com.ktb.joing.domain.matching.repository.MatchingRepository;
import com.ktb.joing.domain.notification.service.NotificationService;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final ItemRepository itemRepository;
    private final CreatorRepository creatorRepository;
    private final NotificationService notificationService;

    // 기획자가 크리에이터에게 매칭 요청
    public MatchingResponse createMatchingToCreator(MatchingRequestToCreator request, String username) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
        Creator creator = creatorRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 권한 검증 - 요청자가 해당 아이템의 기획자인지 확인
        if (!item.getProductManager().getUsername().equals(username)) {
            throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
        }

        Matching matching = Matching.builder()
                .item(item)
                .creator(creator)
                .sender(MatchingSender.PRODUCT_MANAGER)
                .build();

        matching = matchingRepository.save(matching);
        sendMatchingNotification(matching, MatchingStatus.PENDING);

        return new MatchingResponse(matching);
    }

    // 크리에이터가 기획자(기획안)에게 매칭 요청
    public MatchingResponse createMatchingToItem(MatchingRequestToItem request, String username) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        Creator creator = creatorRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Matching matching = Matching.builder()
                .item(item)
                .creator(creator)
                .sender(MatchingSender.CREATOR)
                .build();

        matching = matchingRepository.save(matching);
        sendMatchingNotification(matching, MatchingStatus.PENDING);

        return new MatchingResponse(matching);
    }

    // 매칭 기록 조회 - 취소, 거절 된 매칭을 제외하고 조회
    public List<MatchingListResponse> getMatchingList(String username) {
        List<Matching> matchings = matchingRepository.findActiveMatchingsByUsername(username);

        return matchings.stream()
                .map(matching -> MatchingListResponse.builder()
                        .matchingId(matching.getId())
                        .title(matching.getItem().getTitle())
                        .status(matching.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    // 매칭 자세한 내용 - 매칭 수락시 화면
    public MatchingDetailResponse getMatching(Long matchingId, String username) {
        // 매칭 정보 조회
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));

        // 접근 권한 확인 - 매칭의 크리에이터이거나 기획자만 조회 가능
        if (!matching.getCreator().getUsername().equals(username) &&
                !matching.getItem().getProductManager().getUsername().equals(username)) {
            throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
        }

        return MatchingDetailResponse.builder()
                .matching(matching)
                .build();
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

        return MatchingResponse.builder()
                .matching(matching)
                .build();
    }

    // 매칭 취소
    public void cancelMatching(Long matchingId, String username) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));

        // 권한 검증 - 매칭을 요청한 사람만 취소 가능
        if (matching.getSender() == MatchingSender.PRODUCT_MANAGER) {
            if (!matching.getItem().getProductManager().getUsername().equals(username)) {
                throw new MatchingException(MatchingErrorCode.MATCHING_REQUEST_NOT_AUTHORIZED);
            }
        } else {
            if (!matching.getCreator().getUsername().equals(username)) {
                throw new MatchingException(MatchingErrorCode.MATCHING_REQUEST_NOT_AUTHORIZED);
            }
        }

        if (matching.getStatus() != MatchingStatus.PENDING) {
            throw new MatchingException(MatchingErrorCode.MATCHING_CANCEL_NOT_ALLOWED);
        }

        matching.cancel();
        matchingRepository.save(matching);

        sendMatchingNotification(matching, MatchingStatus.CANCELED);
    }

    // 매칭 요청에 대한 응답 - 매칭 상태 변경 (수락/ 거절)
    public MatchingResponse updateMatchingStatus(Long matchingId, MatchingStatus newStatus, String username) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new MatchingException(MatchingErrorCode.MATCHING_NOT_FOUND));

        // 권한 검증 - 매칭 요청을 받은 사람만 응답 가능
        if (!matching.isReceiver(username)) {
            throw new MatchingException(MatchingErrorCode.MATCHING_NOT_AUTHORIZED);
        }

        if (matching.getStatus() != MatchingStatus.PENDING) {
            throw new MatchingException(MatchingErrorCode.MATCHING_ALREADY_PROCESSED);
        }

        if (newStatus != MatchingStatus.ACCEPTED && newStatus != MatchingStatus.REJECTED) {
            throw new MatchingException(MatchingErrorCode.INVALID_MATCHING_STATUS);
        }

        // 매칭 수락시 다른 대기중인 매칭들은 자동으로 매칭 취소
        if (newStatus == MatchingStatus.ACCEPTED) {
            matching.getItem().match();

            matchingRepository.findByItemAndStatus(matching.getItem(), MatchingStatus.PENDING)
                    .forEach(m -> {
                        if (!m.getId().equals(matchingId)) {
                            m.updateStatus(MatchingStatus.CANCELED);
                            sendMatchingNotification(m, MatchingStatus.CANCELED);
                        }
                    });
        }

        matching.updateStatus(newStatus);
        matching = matchingRepository.save(matching);

        sendMatchingNotification(matching, newStatus);

        return new MatchingResponse(matching);
    }

    private void sendMatchingNotification(Matching matching, MatchingStatus action) {
        String notificationContent;
        String relatedUrl = "/matching/" + matching.getId();
        User receiver;
        boolean isProductManagerSender = matching.getSender() == MatchingSender.PRODUCT_MANAGER;

        switch(action) {
            case PENDING:
                if (isProductManagerSender) {
                    notificationContent = String.format("[매칭 요청] %s님이 '%s' 기획안에 대해 매칭을 요청했습니다.",
                            matching.getItem().getProductManager().getNickname(),
                            matching.getItem().getTitle());
                    receiver = matching.getCreator();
                } else {
                    notificationContent = String.format("[매칭 요청] %s님이 '%s' 기획안에 참여하고 싶어합니다.",
                            matching.getCreator().getNickname(),
                            matching.getItem().getTitle());
                    receiver = matching.getItem().getProductManager();
                }
                break;

            case CANCELED:
                if (isProductManagerSender) {
                    notificationContent = String.format("[매칭 취소] '%s' 기획안에 대한 매칭 요청이 취소되었습니다.",
                            matching.getItem().getTitle());
                    receiver = matching.getCreator();
                } else {
                    notificationContent = String.format("[매칭 취소] '%s' 기획안에 대한 참여 요청이 취소되었습니다.",
                            matching.getItem().getTitle());
                    receiver = matching.getItem().getProductManager();
                }
                break;

            case ACCEPTED:
            case REJECTED:
                String actionStr = (action.toString().equals("ACCEPTED")) ? "수락" : "거절";
                if (isProductManagerSender) {
                    notificationContent = String.format("[매칭 %s] '%s' 기획안에 대한 매칭 요청이 %s되었습니다.",
                            actionStr, matching.getItem().getTitle(), actionStr);
                    receiver = matching.getItem().getProductManager();
                } else {
                    notificationContent = String.format("[매칭 %s] '%s' 기획안에 대한 참여 요청이 %s되었습니다.",
                            actionStr, matching.getItem().getTitle(), actionStr);
                    receiver = matching.getCreator();
                }
                break;

            default:
                throw new MatchingException(MatchingErrorCode.INVALID_MATCHING_STATUS);
        }

        notificationService.send(receiver, notificationContent, relatedUrl);
    }
}
