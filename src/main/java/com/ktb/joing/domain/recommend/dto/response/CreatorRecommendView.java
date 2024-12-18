package com.ktb.joing.domain.recommend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorRecommendView {
    private String profileImage;
    private String nickname;
    private String channelUrl;

    @Builder
    public CreatorRecommendView(String profileImage, String nickname, String channelUrl) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.channelUrl = channelUrl;
    }
}

