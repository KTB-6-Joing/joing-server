package com.ktb.joing.domain.chat.controller;

import com.ktb.joing.domain.chat.dto.request.ChatMessageSocketRequest;
import com.ktb.joing.domain.chat.service.ChatSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatSocketService chatSocketService;

    @MessageMapping("/rooms/{roomId}")
    public void sendMessage(
            Principal principal,
            @DestinationVariable Long roomId,
            ChatMessageSocketRequest request
    ) {
        chatSocketService.sendChat(principal.getName(), roomId, request);
    }

}
