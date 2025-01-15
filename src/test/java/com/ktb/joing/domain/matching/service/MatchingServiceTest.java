package com.ktb.joing.domain.matching.service;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import com.ktb.joing.domain.matching.repository.MatchingRepository;
import com.ktb.joing.domain.notification.repository.NotificationRepository;
import com.ktb.joing.domain.user.entity.*;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import com.ktb.joing.domain.user.repository.ProductManagerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MatchingServiceTest {
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private MatchingRepository matchingRepository;
    @Autowired
    private ProductManagerRepository productManagerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CreatorRepository creatorRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private Item item;
    private List<Creator> creators;
    private List<Matching> matchings;

    @BeforeEach
    void setUp() {
        //기획자
        ProductManager productManager = ProductManager.builder()
                .username("pm@test.com")
                .nickname("PM")
                .profileSetup(true)
                .socialId("test_social_id")
                .socialProvider(SocialProvider.KAKAO)
                .role(Role.ROLE_USER)
                .build();
        productManagerRepository.save(productManager);

        // 기획안
        item = Item.builder()
                .title("Test Item")
                .content("Test Content")
                .mediaType(MediaType.LONG_FORM)
                .score(0)
                .productManager(productManager)
                .category(Category.ENTERTAINMENT)
                .build();
        itemRepository.save(item);

        //크리에이터
        creators = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Creator creator = Creator.builder()
                    .username("creator" + i + "@test.com")
                    .nickname("Creator " + i)
                    .profileSetup(true)
                    .socialId("test_social_id")
                    .socialProvider(SocialProvider.KAKAO)
                    .role(Role.ROLE_USER)
                    .build();
            creators.add(creator);
            creatorRepository.save(creator);
        }

        // 매칭
        matchings = new ArrayList<>();
        for (Creator creator : creators) {
            Matching matching = Matching.builder()
                    .item(item)
                    .creator(creator)
                    .sender(MatchingSender.PRODUCT_MANAGER)
                    .build();
            matchings.add(matching);
            matchingRepository.saveAndFlush(matching);
        }
    }

    @AfterEach
    void tearDown() {
        // 자식 테이블부터 삭제
        notificationRepository.deleteAll();
        matchingRepository.deleteAll();
        creatorRepository.deleteAll();
        itemRepository.deleteAll();
        productManagerRepository.deleteAll();
    }

    @Test
    @DisplayName("하나의 기획안에 대해 여러 매칭이 동시에 수락할 때, 하나의 매칭만 수락되어야 한다")
    void shouldAcceptOnlyOneMatchingPerItem() throws InterruptedException {
        // given
        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int index = i % creators.size();
            executorService.submit(() -> {
                try {
                    matchingService.updateMatchingStatus(
                            matchings.get(index).getId(),
                            MatchingStatus.ACCEPTED,
                            creators.get(index).getUsername()
                    );
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        List<Matching> allMatchings = matchingRepository.findAll();

        long acceptedCount = allMatchings.stream()
                .filter(matching -> matching.getStatus() == MatchingStatus.ACCEPTED)
                .count();

        assertAll(
                () -> assertThat(acceptedCount).isEqualTo(1), // 하나의 매칭만 수락되어야 함 - 데이터베이스에 저장된 상태를 확인
                () -> assertThat(successCount.get()).isEqualTo(1), // 매칭 수락 요청이 한 번만 성공했는지 확인 - 서비스 레벨에서의 동작 검증
                () -> assertThat(failCount.get()).isEqualTo(threadCount - 1) // 나머지는 실패해야 함
        );

        System.out.println("전체 매칭 수: " + allMatchings.size());
        System.out.println("수락된 매칭 수: " + acceptedCount);
        System.out.println("수락 성공한 요청 수: " + successCount.get());
        System.out.println("수락 실패한 요청 수: " + failCount.get());
    }
}
