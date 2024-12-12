package com.ktb.joing.domain.recommend.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class RecommendException extends BusinessException {
    private final RecommendErrorCode recommendErrorCode;

    public RecommendException(RecommendErrorCode recommendErrorCode) {
        super(recommendErrorCode);
        this.recommendErrorCode = recommendErrorCode;
    }
}
