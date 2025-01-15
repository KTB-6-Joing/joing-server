package com.ktb.joing.domain.user.entity;

import com.ktb.joing.domain.user.dto.request.CreatorUpdateRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "CREATOR")
@SuperBuilder
public class Creator extends User{

    private String channelId;

    private String channelUrl;

    private Long maxViews;

    private Long minViews;

    private Long subscribers;

    private Long comments;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private Category category;

    public void update(CreatorUpdateRequest request) {
        if (request.nickname() != null) {
            updateNickname(request.nickname());
        }
        if (request.email() != null) {
            updateEmail(request.email());
        }
        if (request.mediaType() != null) {
            this.mediaType = request.mediaType();
        }
        if (request.category() != null) {
            this.category = request.category();
        }
        if (request.channelId() != null) {
            this.channelId = request.channelId();
        }
        if (request.channelUrl() != null) {
            this.channelUrl = request.channelUrl();
        }
        if (request.profileImage() != null) {
            updateProfileImage(request.profileImage());
        }
        if (request.subscribers() != null){
            this.subscribers = request.subscribers();
        }
    }

}
