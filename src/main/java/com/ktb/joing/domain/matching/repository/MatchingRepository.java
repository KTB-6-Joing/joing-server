package com.ktb.joing.domain.matching.repository;

import com.ktb.joing.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
}
