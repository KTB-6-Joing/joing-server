package com.ktb.joing.domain.item.dto.request;

import jakarta.validation.constraints.Size;

public record EtcRequest(
        @Size(max = 50)
        String name,
        @Size(max = 200)
        String value
) {}
