package com.ktb.joing.domain.matching.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchingErrorCode implements ErrorCode {
    // Matching Error
    // 매칭을 찾을 수 없는 경우
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭 정보를 찾을 수 없습니다."),

    // 권한 관련 에러
    MATCHING_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "매칭에 대한 권한이 없습니다."),
    MATCHING_REQUEST_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "매칭 요청에 대한 권한이 없습니다."),

    // 매칭 상태 관련 에러
    INVALID_MATCHING_STATUS(HttpStatus.BAD_REQUEST, "잘못된 매칭 상태입니다."),
    MATCHING_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 매칭입니다."),

    // 매칭 생성 관련 에러
    INVALID_MATCHING_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 매칭 요청입니다."),
    MATCHING_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 매칭입니다."),

    // 매칭 취소 관련 에러
    MATCHING_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "취소할 수 없는 매칭 상태입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
