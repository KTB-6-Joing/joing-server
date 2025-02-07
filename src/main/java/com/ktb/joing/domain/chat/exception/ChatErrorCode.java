package com.ktb.joing.domain.chat.exception;

import com.ktb.joing.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다"),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "자신이 참여한 채팅방에만 메시지를 보낼 수 있습니다."),
    ROOM_ACCESS_DENIED(HttpStatus.CONFLICT, "이미 참가한 채팅방입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
