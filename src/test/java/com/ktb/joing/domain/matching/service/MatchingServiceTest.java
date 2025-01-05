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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

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
        for (int i = 0; i < 5; i++) {
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
        notificationRepository.deleteAll(); // Notification 먼저 삭제
        matchingRepository.deleteAll();
        creatorRepository.deleteAll(); // JPA에서 연결 삭제 안되는지 확인
        itemRepository.deleteAll(); //
        productManagerRepository.deleteAll();//
    }


    //"동시에 여러 사용자가 매칭 상태를 변경할 때 동시성 문제 테스트"
    //"동시에 여러 크리에이터가 같은 아이템에 매칭 요청할 때 동시성 테스트"
//    @Test
//    @DisplayName("동시에 100개의 매칭 수락 요청시 하나만 성공하는지 테스트")
    @Test
    @DisplayName("동시성 문제 확인 - 여러 매칭이 동시에 ACCEPTED 상태가 되는지 테스트")
    void updateMatchingStatus() throws InterruptedException{
        // given
        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int index = i % creators.size(); // 5명의 크리에이터가 번갈아가며 요청
            executorService.submit(() -> {
                try {
                    matchingService.updateMatchingStatus(
                            matchings.get(index).getId(),
                            MatchingStatus.ACCEPTED,
                            creators.get(index).getUsername()
                    );
                } catch (Exception e) {
                    // Expected exceptions for concurrent requests
                    System.out.println("Creator " + index + " failed: " + e.getMessage()); // 이게 뜻하는게 뭐지?
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        List<Matching> allMatchings = matchingRepository.findAll();
        int acceptedCount = 0;

        for (Matching matching : allMatchings) {
            if (matching.getStatus() == MatchingStatus.ACCEPTED) {
                acceptedCount++;
            }
        }
        assertThat(acceptedCount).isGreaterThan(1); //

        System.out.println("Total matchings: " + allMatchings.size());
        System.out.println("Number of ACCEPTED matchings: " + acceptedCount);
    }
}
