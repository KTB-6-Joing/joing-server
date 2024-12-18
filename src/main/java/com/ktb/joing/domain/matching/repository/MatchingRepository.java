package com.ktb.joing.domain.matching.repository;

import com.ktb.joing.domain.matching.entity.Matching;
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

}
