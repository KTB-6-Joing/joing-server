package com.ktb.joing.domain.matching.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.matching.dto.request.MatchingRequestToCreator;
import com.ktb.joing.domain.matching.dto.request.MatchingRequestToItem;
import com.ktb.joing.domain.matching.dto.request.MatchingStatusRequest;
import com.ktb.joing.domain.matching.dto.response.MatchingResponse;
import com.ktb.joing.domain.matching.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/matching")
public class MatchingController {
    private final MatchingService matchingService;

    @PostMapping("/creator")
    public ResponseEntity<MatchingResponse> requestMatchingToCreator(
            @RequestBody @Valid MatchingRequestToCreator request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchingService.createMatchingToCreator(request, customOAuth2User.getUsername()));
    }

    @PostMapping("/item")
    public ResponseEntity<MatchingResponse> requestMatchingToItem(
            @RequestBody @Valid MatchingRequestToItem request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchingService.createMatchingToItem(request, customOAuth2User.getUsername()));
    }

    @GetMapping("/{matchingId}")
    public ResponseEntity<MatchingResponse> getMatchingStatus(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(
                matchingService.getMatchingStatus(matchingId, customOAuth2User.getUsername())
        );
    }

    @DeleteMapping("/{matchingId}")
    public ResponseEntity<Void> cancelMatching(
            @PathVariable Long matchingId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        matchingService.cancelMatching(matchingId, customOAuth2User.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{matchingId}/status")
    public ResponseEntity<MatchingResponse> updateMatchingStatus(
            @PathVariable Long matchingId,
            @RequestBody @Valid MatchingStatusRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(
                matchingService.updateMatchingStatus(matchingId, request.getStatus(), customOAuth2User.getUsername())
        );
    }

}
