package com.ktb.joing.domain.item.dto.response;

public record EvaluationResponse<T>(
        ResponseType type,
        T data
) {}
