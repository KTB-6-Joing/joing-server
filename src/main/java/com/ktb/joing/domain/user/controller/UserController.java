package com.ktb.joing.domain.user.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.dto.request.UserUpdateRequest;
import com.ktb.joing.domain.user.dto.response.CreatorResponse;
import com.ktb.joing.domain.user.dto.response.ProductManagerResponse;
import com.ktb.joing.domain.user.dto.response.SignupResponse;
import com.ktb.joing.domain.user.dto.response.ProfileEvaluationResponse;
import com.ktb.joing.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/creator")
    public ResponseEntity<SignupResponse> creatorSignUp(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody @Valid CreatorSignupRequest creatorSignupRequest) {

        SignupResponse response = userService.creatorSignUp(customOAuth2User.getUsername(), creatorSignupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signup/productmanager")
    public ResponseEntity<SignupResponse> productManagerSignUp(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody @Valid ProductManagerSignupRequest productManagerSignupRequest){

        SignupResponse response = userService.productManagerSignUp(customOAuth2User.getUsername(), productManagerSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/creator")
    public ResponseEntity<CreatorResponse> getCreatorInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        CreatorResponse response = userService.getCreatorInfo(customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productmanager")
    public ResponseEntity<ProductManagerResponse> getProductManagerInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        ProductManagerResponse response = userService.getProductManagerInfo(customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/creator")
    public ResponseEntity<CreatorResponse> updateCreator(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody UserUpdateRequest request) {
        CreatorResponse response = userService.updateCreator(customOAuth2User.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/productmanager")
    public ResponseEntity<ProductManagerResponse> updateProductManager(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @RequestBody UserUpdateRequest request) {
        ProductManagerResponse response = userService.updateProductManager(customOAuth2User.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluation")
    public Mono<ResponseEntity<ProfileEvaluationResponse>> profileEvaluation(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return userService.profileEvaluation(customOAuth2User.getUsername())
                .map(ResponseEntity::ok);
    }

}
