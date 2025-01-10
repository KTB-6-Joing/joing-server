package com.ktb.joing.domain.notification.dto.response;

import com.ktb.joing.domain.notification.entity.Notification;

public record NotificationResponse(
        Long notificationId,
        String content,
        String relatedUrl
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getContent(),
                notification.getRelatedUrl()
        );
    }
}
