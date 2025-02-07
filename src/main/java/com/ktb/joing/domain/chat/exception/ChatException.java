package com.ktb.joing.domain.chat.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class ChatException  extends BusinessException {
    private final ChatErrorCode chatErrorCode;

    public ChatException(ChatErrorCode chatErrorCode) {
        super(chatErrorCode);
        this.chatErrorCode = chatErrorCode;
    }
}
