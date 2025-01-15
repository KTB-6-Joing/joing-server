package com.ktb.joing.domain.item.repository;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendView;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :itemId")
    Optional<Item> findByIdWithPessimisticLock(@Param("itemId") Long itemId);

    @Query("SELECT new com.ktb.joing.domain.recommend.dto.response.ItemRecommendView(" +
            "i.id, " +
            "i.title, " +
            "i.summary.content, " +
            "i.summary.keyword) " +
            "FROM Item i WHERE i.id = :itemId")
    Optional<ItemRecommendView> findItemRecommendViewById(@Param("itemId") Long itemId);

    List<Item> findByProductManagerUsernameAndCreatedDateTimeGreaterThanEqualOrderByCreatedDateTimeDesc(
            String username, LocalDateTime startDate
    );

    default List<Item> findRecentItems(String username, LocalDateTime startDate) {
        return findByProductManagerUsernameAndCreatedDateTimeGreaterThanEqualOrderByCreatedDateTimeDesc(
                username, startDate
        );
    }
}
