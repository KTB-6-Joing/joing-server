package com.ktb.joing.domain.recommend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorRecommendView {
    private Long id;
    private String profileImage;
    private String nickname;
    private String channelUrl;

    @Builder
    public CreatorRecommendView(Long id, String profileImage, String nickname, String channelUrl) {
        this.id = id;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.channelUrl = channelUrl;
    }
}

