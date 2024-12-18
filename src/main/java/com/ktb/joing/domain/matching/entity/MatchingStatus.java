package com.ktb.joing.domain.matching.entity;

public enum MatchingStatus {
    PENDING,    // 대기 중(요청했을때)
    ACCEPTED,   // 수락됨
    REJECTED,   // 거절됨
    CANCELED   // 취소됨
}
