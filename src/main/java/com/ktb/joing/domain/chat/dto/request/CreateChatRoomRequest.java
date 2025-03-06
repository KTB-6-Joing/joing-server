package com.ktb.joing.domain.chat.dto.request;

public record CreateChatRoomRequest(
   Long matchingId,
   String sender
) {}
