package com.ktb.joing.domain.notification.entity;

import com.ktb.joing.common.model.BaseTimeEntity;
import com.ktb.joing.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String relatedUrl;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @Builder
    public Notification(String content, String relatedUrl, boolean isRead, User receiver) {
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.isRead = isRead;
        this.receiver = receiver;
    }

    public void read() {
        isRead = true;
    }

}
