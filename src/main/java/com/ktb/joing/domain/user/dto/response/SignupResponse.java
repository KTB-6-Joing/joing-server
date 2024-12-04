package com.ktb.joing.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {
    private final String type;

    public SignupResponse(String type) {
        this.type = type;
    }
}
