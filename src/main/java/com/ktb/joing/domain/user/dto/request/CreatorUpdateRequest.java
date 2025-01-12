package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreatorUpdateRequest(
        @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다")
        String nickname,
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String email,
        String channelId,
        String channelUrl,
        Long subscribers,
        String profileImage,
        MediaType mediaType,
        Category category
) {}
