package com.ktb.joing.domain.chat.controller;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.chat.dto.request.CreateChatRoomRequest;
import com.ktb.joing.domain.chat.dto.response.ChatRoomDetailResponse;
import com.ktb.joing.domain.chat.dto.response.CreateChatRoomResponse;
import com.ktb.joing.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping()
    public ResponseEntity<CreateChatRoomResponse> createChatRoom(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody CreateChatRoomRequest request
    ) {
        CreateChatRoomResponse response = chatRoomService.createChatRoom(customOAuth2User.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 채팅방 나가기
    @PostMapping("/leave/{roomId}")
    public ResponseEntity<Void> leave(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable Long roomId
    ) {
        chatRoomService.leave(customOAuth2User.getUsername(), roomId);
        return ResponseEntity.noContent().build();
    }

    // 참여 채팅방 목록 조회
    @GetMapping()
    public ResponseEntity<List<ChatRoomDetailResponse>> findChatRooms(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        List<ChatRoomDetailResponse> response = chatRoomService.findChatRooms(customOAuth2User.getUsername());
        return ResponseEntity.ok(response);
    }

}
