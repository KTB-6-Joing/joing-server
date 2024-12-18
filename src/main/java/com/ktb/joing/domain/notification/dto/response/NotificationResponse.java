package com.ktb.joing.domain.notification.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponse {
    private Long notificationId;
    private String content;
    private String relatedUrl;

    @Builder
    public NotificationResponse(Long notificationId, String content, String relatedUrl) {
        this.notificationId = notificationId;
        this.content = content;
        this.relatedUrl = relatedUrl;
    }
}
