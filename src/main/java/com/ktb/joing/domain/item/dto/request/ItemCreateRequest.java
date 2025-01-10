package com.ktb.joing.domain.item.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ItemCreateRequest(
        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 100)
        String title,

        @NotBlank(message = "내용은 필수입니다")
        @Size(max = 2500)
        String content,

        @NotNull(message = "미디어 타입은 필수입니다")
        MediaType mediaType,

        @NotNull(message = "카테고리는 필수입니다")
        Category category,

        List<EtcRequest> etcs
) {}
