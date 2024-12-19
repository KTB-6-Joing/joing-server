package com.ktb.joing.domain.user.repository;

import com.ktb.joing.domain.recommend.dto.response.CreatorRecommendView;
import com.ktb.joing.domain.user.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByUsername(String username);

    @Query("SELECT new com.ktb.joing.domain.recommend.dto.response.CreatorRecommendView(" +
            "c.profileImage, " +
            "c.nickname, " +
            "c.channelUrl) " +
            "FROM Creator c WHERE c.id = :creatorId")
    Optional<CreatorRecommendView> findCreatorRecommendViewById(@Param("creatorId") Long creatorId);
}
