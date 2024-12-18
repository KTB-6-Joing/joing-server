package com.ktb.joing.domain.recommend.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendView;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendView;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/items/{itemId}")
    public Mono<ResponseEntity<List<CreatorRecommendView>>> getRecommendedCreators(@PathVariable @Valid Long itemId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return recommendService.getRecommendedCreators(itemId, customOAuth2User.getUsername())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/users")
    public  Mono<ResponseEntity<List<ItemRecommendView>>> getRecommendedItems(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return recommendService.getRecommendedItems(customOAuth2User.getUsername())
                .map(ResponseEntity::ok);
    }

}
