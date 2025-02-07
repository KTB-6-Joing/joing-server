package com.ktb.joing.domain.chat.dto.request;

public record CreateChatRoomRequest(
   Long receiverId,
   Long itemId,
   String sender
) {}
