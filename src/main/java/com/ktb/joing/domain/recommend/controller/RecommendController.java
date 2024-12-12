package com.ktb.joing.domain.recommend.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendResponse;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendResponse;
import com.ktb.joing.domain.recommend.service.RecommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/items/{itemId}")
    public Mono<ResponseEntity<CreatorRecommendResponse>> getRecommendedCreators(@PathVariable @Valid Long itemId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return recommendService.getRecommendedCreators(itemId, customOAuth2User.getUsername())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/users/{userId}")
    public  Mono<ResponseEntity<ItemRecommendResponse>> getRecommendedItems(@PathVariable @Valid Long userId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return recommendService.getRecommendedItems(userId, customOAuth2User.getUsername())
                .map(ResponseEntity::ok); // 수정
    }

}
