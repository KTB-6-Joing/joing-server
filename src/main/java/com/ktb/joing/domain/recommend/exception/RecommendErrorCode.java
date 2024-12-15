package com.ktb.joing.domain.recommend.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecommendErrorCode implements ErrorCode {
    // Recommend Error
    AI_RECOMMEND_FAILED(HttpStatus.BAD_GATEWAY, "AI 추천 요청에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
