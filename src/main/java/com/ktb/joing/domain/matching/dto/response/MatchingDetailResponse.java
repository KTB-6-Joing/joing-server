package com.ktb.joing.domain.matching.dto.response;

import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.entity.MatchingSender;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingDetailResponse {
    private String productManagerNickname;
    private String productManagerEmail;
    private Long itemId;
    private String itemTitle;
    private String itemContent;
    private List<String> itemKeyword;

    private String creatorNickname;
    private String creatorProfileImage;
    private String creatorEmail;
    private String creatorChannelUrl;

    private MatchingStatus status;
    private MatchingSender sender;

    @Builder
    public MatchingDetailResponse(Matching matching){
        this.productManagerNickname = matching.getItem().getProductManager().getNickname();
        this.productManagerEmail = matching.getItem().getProductManager().getEmail();

        this.itemId = matching.getItem().getId();
        this.itemTitle = matching.getItem().getTitle();
        this.itemContent = matching.getItem().getSummary().getContent();
        this.itemKeyword = Arrays.asList(matching.getItem().getSummary().getKeyword().split(","));

        this.creatorNickname = matching.getCreator().getNickname();
        this.creatorProfileImage = matching.getCreator().getProfileImage();
        this.creatorEmail = matching.getCreator().getEmail();
        this.creatorChannelUrl = matching.getCreator().getChannelUrl();

        this.status = matching.getStatus();
        this.sender = matching.getSender();
    }
}
