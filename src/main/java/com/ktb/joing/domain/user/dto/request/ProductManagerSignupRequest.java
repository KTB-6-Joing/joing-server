package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductManagerSignupRequest(
        @NotBlank
        String nickname,
        @NotBlank
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String email,
        @NotNull
        List<Category> favoriteCategories
) {}
