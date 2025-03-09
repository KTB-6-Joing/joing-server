package com.ktb.joing.domain.user.service;

import com.ktb.joing.common.exception.AiException;
import com.ktb.joing.domain.auth.entity.TempUser;
import com.ktb.joing.domain.auth.repository.TempUserRepository;
import com.ktb.joing.domain.user.client.ProfileAIClient;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.CreatorUpdateRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerUpdateRequest;
import com.ktb.joing.domain.user.dto.response.*;
import com.ktb.joing.domain.user.dto.request.ProfileEvaluationRequest;
import com.ktb.joing.domain.user.entity.*;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import com.ktb.joing.domain.user.repository.ProductManagerRepository;
import com.ktb.joing.domain.user.repository.UserRepository;
import jakarta.persistence.DiscriminatorValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final CreatorRepository creatorRepository;
    private final ProductManagerRepository productManagerRepository;
    private final ProfileAIClient profileAIClient;

    // 회원가입(크리에이터)
    @Transactional
    public SignupResponse creatorSignUp(String username, CreatorSignupRequest request) {
        TempUser tempUser = tempUserRepository.findById(username)
                .orElseThrow(() -> new UserException(UserErrorCode.TEMP_USER_NOT_FOUND));

        Creator creator = Creator.builder()
                .username(tempUser.getId())
                .email(request.email())
                .nickname(request.nickname())
                .profileImage(request.profileImage())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .socialId(tempUser.getSocialId())
                .socialProvider(tempUser.getSocialProvider())
                .channelId(request.channelId())
                .channelUrl(request.channelUrl())
                .subscribers(request.subscribers())
                .mediaType(request.mediaType())
                .category(request.category())
                .build();

        userRepository.save(creator);
        tempUserRepository.deleteById(username);

        return new SignupResponse("CREATOR");
    }

    // 회원가입(기획자)
    @Transactional
    public SignupResponse productManagerSignUp(String username, ProductManagerSignupRequest request) {
        TempUser tempUser = tempUserRepository.findById(username)
                .orElseThrow(() -> new UserException(UserErrorCode.TEMP_USER_NOT_FOUND));

        ProductManager user = ProductManager.builder()
                .username(tempUser.getId())
                .profileImage(tempUser.getProfileImage())
                .socialId(tempUser.getSocialId())
                .socialProvider(tempUser.getSocialProvider())
                .email(request.email())
                .nickname(request.nickname())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .build();

        request.favoriteCategories().forEach(category -> {
            FavoriteCategory favoriteCategory = FavoriteCategory.builder()
                    .category(category)
                    .build();
            user.addFavoriteCategory(favoriteCategory);
        });

        userRepository.save(user);
        tempUserRepository.deleteById(username);

        return new SignupResponse("PRODUCT_MANAGER");
    }

    // 회원 정보 조회(크리에이터)
    public CreatorResponse getCreatorInfo(String username) {
        Creator creator = creatorRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return CreatorResponse.from(creator);
    }

    // 회원 정보 조회(기획자)
    public ProductManagerResponse getProductManagerInfo(String username) {
        ProductManager productManager = productManagerRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return ProductManagerResponse.from(productManager);
    }

    // 현재 인증된 계정 정보 조회
    public UserResponse getUserInfo(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String userType = user.getClass().getAnnotation(DiscriminatorValue.class).value();
        return new UserResponse(username, userType);
    }

    // 회원 정보 수정(크리에이터)
    @Transactional
    public CreatorResponse updateCreator(String username, CreatorUpdateRequest request) {
        Creator creator = creatorRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        creator.update(request);
        return CreatorResponse.from(creator);
    }

    // 회원 정보 수정(기획자)
    @Transactional
    public ProductManagerResponse updateProductManager(String username, ProductManagerUpdateRequest request) {
        ProductManager productManager = productManagerRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        productManager.update(request);
        return ProductManagerResponse.from(productManager);
    }

    // 닉네임 중복 확인
    public NicknameAvailableResponse checkNicknameDuplicate(String nickname) {
        boolean available = !userRepository.existsByNickname(nickname);

        return NicknameAvailableResponse.from(available);
    }

    // 크리에이터 프로필(채널) 유해성 검사
    public Mono<ProfileEvaluationResponse> profileEvaluation(String channelId) {
        ProfileEvaluationRequest request = new ProfileEvaluationRequest(channelId);
        return profileAIClient.profileEvaluation(request)
                .onErrorMap(e -> {
                    if (e instanceof AiException) {
                        return e;
                    }
                    return new UserException(UserErrorCode.PROFILE_EVALUATION_FAILED);
                });
    }
}
