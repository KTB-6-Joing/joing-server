package com.ktb.joing.domain.user.service;

import com.ktb.joing.common.exception.AiException;
import com.ktb.joing.domain.auth.entity.TempUser;
import com.ktb.joing.domain.auth.repository.TempUserRepository;
import com.ktb.joing.domain.user.client.ProfileAIClient;
import com.ktb.joing.domain.user.dto.request.CreatorSignupRequest;
import com.ktb.joing.domain.user.dto.request.CreatorUpdateRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerSignupRequest;
import com.ktb.joing.domain.user.dto.request.ProductManagerUpdateRequest;
import com.ktb.joing.domain.user.dto.response.CreatorResponse;
import com.ktb.joing.domain.user.dto.response.NicknameAvailableResponse;
import com.ktb.joing.domain.user.dto.response.ProductManagerResponse;
import com.ktb.joing.domain.user.dto.request.ProfileEvaluationRequest;
import com.ktb.joing.domain.user.dto.response.ProfileEvaluationResponse;
import com.ktb.joing.domain.user.dto.response.SignupResponse;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.FavoriteCategory;
import com.ktb.joing.domain.user.entity.ProductManager;
import com.ktb.joing.domain.user.entity.Role;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.CreatorRepository;
import com.ktb.joing.domain.user.repository.ProductManagerRepository;
import com.ktb.joing.domain.user.repository.UserRepository;
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
                .email(request.getEmail())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .socialId(tempUser.getSocialId())
                .socialProvider(tempUser.getSocialProvider())
                .channelId(request.getChannelId())
                .channelUrl(request.getChannelUrl())
                .subscribers(request.getSubscribers())
                .mediaType(request.getMediaType())
                .category(request.getCategory())
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
                .email(request.getEmail())
                .nickname(request.getNickname())
                .profileSetup(true)
                .role(Role.ROLE_USER)
                .build();

        request.getFavoriteCategories().forEach(category -> {
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
        return CreatorResponse.builder().creator(creator).build();
    }

    // 회원 정보 조회(기획자)
    public ProductManagerResponse getProductManagerInfo(String username) {
        ProductManager productManager = productManagerRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return ProductManagerResponse.builder().productManager(productManager).build();
    }

    // 회원 정보 수정(크리에이터)
    @Transactional
    public CreatorResponse updateCreator(String username, CreatorUpdateRequest request) {
        Creator creator = creatorRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        creator.update(request);
        return CreatorResponse.builder().creator(creator).build();
    }

    // 회원 정보 수정(기획자)
    @Transactional
    public ProductManagerResponse updateProductManager(String username, ProductManagerUpdateRequest request) {
        ProductManager productManager = productManagerRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        productManager.update(request);
        return ProductManagerResponse.builder().productManager(productManager).build();
    }

    // 닉네임 중복 확인
    public NicknameAvailableResponse checkNicknameDuplicate(String nickname) {
        boolean available = !userRepository.existsByNickname(nickname);

        return NicknameAvailableResponse.builder()
                .available(available)
                .build();
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
