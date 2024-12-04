package com.ktb.joing.domain.user.dto.response;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.MediaType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorResponse {
    private String nickname;
    private String email;
    private String profileImage;
    private String channelId;
    private String channelUrl;
    private MediaType mediaType;
    private Category category;

    @Builder
    public CreatorResponse(Creator creator) {
        this.nickname = creator.getNickname();
        this.email = creator.getEmail();
        this.profileImage = creator.getProfileImage();
        this.channelId = creator.getChannelId();
        this.channelUrl = creator.getChannelUrl();
        this.mediaType = creator.getMediaType();
        this.category = creator.getCategory();
    }
}
