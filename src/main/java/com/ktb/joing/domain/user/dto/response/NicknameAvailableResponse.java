package com.ktb.joing.domain.user.dto.response;

public record NicknameAvailableResponse(
        Boolean available
) {
    public static NicknameAvailableResponse from(Boolean available) {
        return new NicknameAvailableResponse(available);
    }
}
