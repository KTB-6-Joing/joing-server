package com.ktb.joing.domain.user.dto.response;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.Creator;
import com.ktb.joing.domain.user.entity.MediaType;

public record CreatorResponse(
        String nickname,
        String email,
        String profileImage,
        String channelId,
        String channelUrl,
        MediaType mediaType,
        Category category
) {
    public static CreatorResponse from(Creator creator) {
        return new CreatorResponse(
                creator.getNickname(),
                creator.getEmail(),
                creator.getProfileImage(),
                creator.getChannelId(),
                creator.getChannelUrl(),
                creator.getMediaType(),
                creator.getCategory()
        );
    }
}
