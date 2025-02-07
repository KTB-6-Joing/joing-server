package com.ktb.joing.common.config;

import com.ktb.joing.domain.auth.dto.CustomOAuth2User;
import com.ktb.joing.domain.auth.dto.UserDto;
import com.ktb.joing.domain.auth.jwt.JwtUtil;
import com.ktb.joing.domain.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Connect 연결 요청이면 InboundChannel에 메세지를 보내기전에 user 인증객체를 등록
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String token = accessor.getFirstNativeHeader("access");
            if (token == null || !jwtUtil.isTokenValid(token)) {
                throw new MessageDeliveryException("Invalid or missing token");
            }
            CustomOAuth2User customOAuth2User = getCustomOAuth2User(jwtUtil.getUsernameFromToken(token),
                    jwtUtil.getRoleFromToken(token));
            Authentication authentication = getAuthentication(customOAuth2User);
            accessor.setUser(authentication);
        }
        return message;
    }

    private Authentication getAuthentication(CustomOAuth2User customOAuth2User) {
        return new UsernamePasswordAuthenticationToken(customOAuth2User, "", customOAuth2User.getAuthorities());
    }

    private CustomOAuth2User getCustomOAuth2User(String userId, String role) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userId);
        userDto.setRole(Role.valueOf(role));
        return new CustomOAuth2User(userDto);
    }
}
