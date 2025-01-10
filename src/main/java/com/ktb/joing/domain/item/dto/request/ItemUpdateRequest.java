package com.ktb.joing.domain.item.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ItemUpdateRequest(
        @Size(max = 100)
        String title,
        @Size(max = 2500)
        String content,
        MediaType mediaType,
        Category category,
        List<EtcRequest> etcs
) {}
