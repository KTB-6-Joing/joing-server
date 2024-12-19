package com.ktb.joing.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameAvailableResponse {
    private Boolean available;

    @Builder
    public NicknameAvailableResponse(Boolean available) {
        this.available = available;
    }
}
