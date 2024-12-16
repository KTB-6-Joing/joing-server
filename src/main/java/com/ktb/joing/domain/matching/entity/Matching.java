package com.ktb.joing.domain.matching.entity;

import com.ktb.joing.common.model.BaseTimeEntity;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.user.entity.Creator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Creator creator;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;

    @Enumerated(EnumType.STRING)
    private MatchingSender sender;

    @Builder
    public Matching(Item item, Creator creator, MatchingSender sender) {
        this.item = item;
        this.creator = creator;
        this.status = MatchingStatus.PENDING;
        this.sender = sender;
    }

    public void updateStatus(MatchingStatus status) {
        this.status = status;
    }

    public void cancel() {
        this.status = MatchingStatus.CANCELED;
    }

    public boolean isReceiver(String username) {
        return sender == MatchingSender.PRODUCT_MANAGER ?
                creator.getUsername().equals(username) :
                item.getProductManager().getUsername().equals(username);
    }
}
