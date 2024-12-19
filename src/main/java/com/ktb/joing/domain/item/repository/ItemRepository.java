package com.ktb.joing.domain.item.repository;

import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.recommend.dto.response.ItemRecommendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT new com.ktb.joing.domain.recommend.dto.response.ItemRecommendView(" +
            "i.id, " +
            "i.title, " +
            "i.content, " +
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
