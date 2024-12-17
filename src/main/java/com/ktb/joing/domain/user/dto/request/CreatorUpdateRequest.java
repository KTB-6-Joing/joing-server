package com.ktb.joing.domain.user.dto.request;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.MediaType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorUpdateRequest {
    @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다")
    private String nickname;

    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    private String channelId;

    private String channelUrl;

    private String channelImage;

    private MediaType mediaType;

    private Category category;

    @Builder
    public CreatorUpdateRequest(String nickname, String email, String channelId, String channelUrl, String channelImage, MediaType mediaType, Category category){
        this.nickname = nickname;
        this.email = email;
        this.channelId = channelId;
        this.channelUrl = channelUrl;
        this.channelImage = channelImage; //
        this.mediaType = mediaType;
        this.category = category;
    }
}
