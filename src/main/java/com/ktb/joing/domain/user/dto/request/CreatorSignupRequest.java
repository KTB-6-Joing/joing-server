package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatorSignupRequest(
        @NotBlank
        String nickname,
        @NotBlank
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String email,
        String channelId,
        String channelUrl,
        Long subscribers,
        String profileImage,
        MediaType mediaType,
        Category category
) {}