package com.ktb.joing.domain.matching.repository;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.matching.entity.Matching;
import com.ktb.joing.domain.matching.entity.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    @Query("SELECT m FROM Matching m " +
            "WHERE (m.creator.username = :username OR m.item.productManager.username = :username) " +
            "AND m.status NOT IN (:#{T(com.ktb.joing.domain.matching.entity.MatchingStatus).CANCELED}, " +
            ":#{T(com.ktb.joing.domain.matching.entity.MatchingStatus).REJECTED})")
    List<Matching> findActiveMatchingsByUsername(@Param("username") String username);

    List<Matching> findByItemAndStatus(Item item, MatchingStatus status);

    @Query("SELECT EXISTS (SELECT 1 FROM Matching m " +
            "WHERE m.item.id = :itemId AND m.creator.id = :creatorId " +
            "AND m.status NOT IN (:#{T(com.ktb.joing.domain.matching.entity.MatchingStatus).CANCELED}))")
    boolean isMatched(@Param("itemId") Long itemId, @Param("creatorId") Long creatorId);
}
