package com.ktb.joing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {
    //AI
    AI_VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "입력값 검증에 실패했습니다"),
    AI_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    AI_BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "AI 서버와의 통신에 실패하였습니다."),
    AI_INVALID_CHANNEL_ID_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "유효하지 않은 형식의 채널아이디입니다"),
    AI_INVALID_CHANNEL_ID(HttpStatus.UNPROCESSABLE_ENTITY, "유효하지 않은 채널아이디입니다"),
    AI_INSUFFICIENT_VIDEOS(HttpStatus.UNPROCESSABLE_ENTITY, "영상의 개수가 충분하지 않아 더이상의 평가가 불가능합니다");


    private final HttpStatus httpStatus;
    private final String message;
}
