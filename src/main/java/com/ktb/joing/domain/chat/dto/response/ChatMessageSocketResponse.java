package com.ktb.joing.domain.chat.dto.response;

import com.ktb.joing.domain.chat.entity.MessageType;
import com.ktb.joing.domain.user.entity.User;

import java.time.LocalDateTime;

public record ChatMessageSocketResponse(
        MessageType messageType,
        String content,
        Long senderId,
        String senderName,
        String profileImage,
        LocalDateTime createdAt
) {
    public ChatMessageSocketResponse(
            MessageType messageType,
            String content,
            User senderUser,
            LocalDateTime createdAt
    ) {
        this(
                messageType,
                content,
                senderUser.getId(),
                senderUser.getNickname(),
                senderUser.getProfileImage(),
                createdAt
        );
    }
}
