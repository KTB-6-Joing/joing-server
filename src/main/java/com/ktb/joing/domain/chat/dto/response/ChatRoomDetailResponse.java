package com.ktb.joing.domain.chat.dto.response;

import java.time.LocalDateTime;

public record ChatRoomDetailResponse(
    Long roomId,
    String receiverName,
    String receiverProfileImage,
    String recentMessage,
    LocalDateTime recentMessageCreateAt
) {}
