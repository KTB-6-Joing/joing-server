package com.ktb.joing.common.exception;

import lombok.Getter;

@Getter
public class AiException extends BusinessException {
    private final AiErrorCode aiErrorCode;
    private final String errorDetail;

    public AiException(AiErrorCode aiErrorCode, String errorDetail) {
        super(aiErrorCode);
        this.aiErrorCode = aiErrorCode;
        this.errorDetail = errorDetail;
    }
}
